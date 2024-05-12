(ns stethoscope.adapters.time-window
  (:require [schema.core :as s]))

(s/defn status->wire :- s/Str
  [status :- s/Keyword]
  (case status
    :success "success"
    :failure "failure"))

(s/defn status-wire->internal :- s/Keyword
  [status :- s/Str]
  (case status
    "success" :success
    "failure" :failure))