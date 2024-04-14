(ns stethoscope.db.postgresql.health-check
  (:require [schema.core :as s]
            [next.jdbc :as jdbc]
            [stethoscope.adapters.health-check :as adapters.health-check]))

(s/defn insert!
  [{:keys [id status code requested-at] :as health-check}
   db-connection]
  (jdbc/execute! db-connection ["INSERT INTO healthcheck (id, status, code, requested_at) VALUES (?, ?, ?, ?)"
                                id (adapters.health-check/status->wire status) code requested-at])
  health-check)