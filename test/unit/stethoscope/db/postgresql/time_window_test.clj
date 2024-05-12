(ns stethoscope.db.postgresql.time-window-test
  (:require [clojure.test :refer :all]
            [common-clj.component.postgresql :as component.postgresql]
            [java-time.api :as jt]
            [schema.test :as s]
            [stethoscope.db.postgresql.time-window :as database.time-window]
            [matcher-combinators.test :refer [match?]]))

(s/deftest insert-test
  (testing "Insert time-window entity on database"
    (let [{:keys [database-connection]} (component.postgresql/posgresql-component-for-unit-tests "resources/schema.sql")]
      (database.time-window/insert! :success (jt/local-date-time) database-connection)

      (is (match? {:id         uuid?
                   :started-at inst?
                   :status     :success}
                  (database.time-window/latest-one database-connection))))))

(s/deftest latest-one-test
  (testing "Query the latest time-window"
    (let [{:keys [database-connection]} (component.postgresql/posgresql-component-for-unit-tests "resources/schema.sql")]
      (database.time-window/insert! :success (jt/local-date-time) database-connection)
      (database.time-window/insert! :failure (jt/local-date-time) database-connection)

      (is (match? {:id         uuid?
                   :started-at inst?
                   :status     :failure}
                  (database.time-window/latest-one database-connection)))

      (database.time-window/insert! :success (jt/local-date-time) database-connection)

      (is (match? {:id         uuid?
                   :started-at inst?
                   :status     :success}
                  (database.time-window/latest-one database-connection)))))

  (testing "Query the latest time-window (empty database)"
    (let [{:keys [database-connection]} (component.postgresql/posgresql-component-for-unit-tests "resources/schema.sql")]

      (is (match? nil?
                  (database.time-window/latest-one database-connection))))))

(s/deftest set-ended-at-test
  (testing "Set ended-at for time-window entity"
    (let [{:keys [database-connection]} (component.postgresql/posgresql-component-for-unit-tests "resources/schema.sql")]
      (database.time-window/insert! :success (jt/local-date-time) database-connection)
      (-> (database.time-window/latest-one database-connection)
          :id
          (database.time-window/set-ended-at! (jt/local-date-time) database-connection))

      (is (match? {:id         uuid?
                   :started-at inst?
                   :ended-at   inst?
                   :status     :success}
                  (database.time-window/latest-one database-connection))))))
