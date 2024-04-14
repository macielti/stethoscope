(ns stethoscope.controllers.health-check
  (:require [clj-http.client :as client]
            [stethoscope.adapters.health-check :as adapters.health-check]
            [stethoscope.db.postgresql.health-check :as database.health-check]
            [schema.core :as s]))

(s/defn perform-health-check!
  [db-connection]
  (-> (client/get "https://sigaa.ufpi.br/sigaa/verTelaLogin.do" {:throw-exceptions false})
      adapters.health-check/->health-check
      (database.health-check/insert! db-connection)))