(defproject speedster "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.tinkerpop.blueprints/blueprints-core "2.3.0"]
                 [com.tinkerpop.gremlin/gremlin-java       "2.3.0"]
                 [com.tinkerpop.gremlin/gremlin-groovy     "2.3.0"]
                 [http-kit "2.1.5"]
                 [compojure "1.1.5"]
                 [org.clojure/data.json "0.2.2"]]
  :aot [speedster.util]
  :main speedster.core)
