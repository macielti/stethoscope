(defproject stethoscope "0.1.0-SNAPSHOT"

  :description "Service in order to collect data about SIGAA UFPI availability"

  :url "https://github.com/macielti/stethoscope"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clojure.java-time "1.4.2"]
                 [net.clojars.macielti/common-clj "25.49.48"]]

  :profiles {:test {:injections [(require 'hashp.core)]}}

  :injections [(require 'hashp.core)]

  :repl-options {:init-ns stethoscope.components}

  :test-paths ["test/unit" "test/integration" "test/helpers"]

  :main stethoscope.components)
