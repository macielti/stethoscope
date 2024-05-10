(ns stethoscope.components
  (:require [com.stuartsierra.component :as component]
            [common-clj.component.config :as component.config]
            [common-clj.component.postgresql :as component.postgresql]
            [stethoscope.jobs :as jobs])
  (:gen-class))

(def system
  (component/system-map
    :config (component.config/new-config "resources/config.example.edn" :test :edn)
    #_:postgresql #_(component/using (component.postgresql/new-postgresql) [:config])
    :jobs (component/using (jobs/new-jobs) [:config #_:postgresql])))

(defn start-system! []
  (component/start system))

(def -main start-system!)