(ns stethoscope.db.postgresql.time-window
  (:require [medley.core :as medley]
            [next.jdbc :as jdbc]
            [schema.core :as s]
            [stethoscope.adapters.time-window :as adapters.time-window])
  (:import (java.time LocalDateTime)))

(s/defn insert!
  [status :- s/Keyword
   started-at :- LocalDateTime
   db-connection]
  (jdbc/execute! db-connection ["INSERT INTO time_window (time_window__id, time_window__started_at, time_window__status) VALUES (?, ?, ?)"
                                (random-uuid) started-at (adapters.time-window/status->wire status)]))

(s/defn latest-one
  [db-connection]
  (let [{:time_window/keys [time_window__id
                            time_window__started_at
                            time_window__ended_at
                            time_window__status]} (jdbc/execute-one! db-connection ["SELECT * FROM time_window
                                                                                     ORDER BY time_window__started_at desc"])]
    (when time_window__id
      (medley/assoc-some {:id         time_window__id
                          :started-at time_window__started_at
                          :status     (adapters.time-window/status-wire->internal time_window__status)}
                         :ended-at time_window__ended_at))))

(s/defn set-ended-at!
  [id :- s/Uuid
   started-at :- LocalDateTime
   db-connection]
  (jdbc/execute-one! db-connection ["UPDATE time_window
                                     SET
                                       time_window__ended_at = ?
                                     WHERE time_window__id = ?"
                                    started-at id]))