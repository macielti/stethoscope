(ns stethoscope.controllers.health-check
  (:require [clj-http.client :as client]
            [java-time.api :as jt]
            [stethoscope.db.postgresql.time-window :as database.time-window]
            [schema.core :as s]
            [taoensso.timbre :as log]))

(s/defn response->result :- s/Keyword
  [response :- s/Any]
  (if (= (:status response) 200)
    :success
    :failure))

(s/defn perform-health-check!
  [db-connection]
  (let [result (-> (client/get "https://sigaa.ufpi.br/sigaa/verTelaLogin.do" {:throw-exceptions false})
                   response->result)
        maybe-latest-one (database.time-window/latest-one db-connection)]
    (log/info result)
    (cond
      (nil? maybe-latest-one)
      (database.time-window/insert! result (jt/local-date-time) db-connection)

      (not= (:status maybe-latest-one) result)
      (do (database.time-window/set-ended-at! (:id maybe-latest-one) (jt/local-date-time) db-connection)
          (database.time-window/insert! result (jt/local-date-time) db-connection)))))