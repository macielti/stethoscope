(ns stethoscope.jobs
  (:require [com.stuartsierra.component :as component]
            [stethoscope.controllers.health-check :as controllers.health-check]
            [overtone.at-at :as at-at]
            [taoensso.timbre :as log]))

(defrecord Jobs [config postgresql]
  component/Lifecycle
  (start [component]
    (let [pool (at-at/mk-pool)]

      (at-at/interspaced 15000 #(try (controllers.health-check/perform-health-check! (:postgresql postgresql))
                                     (catch Exception ex
                                       (log/error ex))) pool)

      (merge component {:jobs {:pool pool}})))

  (stop [{:keys [service]}]
    (at-at/stop-and-reset-pool! (:pool service) :stop)))

(defn new-jobs []
  (->Jobs {} {}))