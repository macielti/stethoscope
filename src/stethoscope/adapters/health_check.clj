(ns stethoscope.adapters.health-check
  (:require [java-time.api :as jt]
            [schema.core :as s]))

(s/defn status->wire
  [status]
  (case status
    :success "success"
    :failure "failure"))

(s/defn ->health-check
  [response :- s/Any]
  {:id         (random-uuid)
   :status     (if (= (:status response) 200)
                 :success
                 :failure)
   :started-at (jt/local-date-time (jt/zone-id "UTC"))
   :ended-at   (jt/local-date-time (jt/zone-id "UTC"))})