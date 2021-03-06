(ns speedster.core
  (:require [compojure.route     :refer [not-found]]
            [compojure.handler   :refer [site]]
            [compojure.core      :refer [defroutes GET POST]]
            [org.httpkit.server  :refer :all]
            [speedster.graph     :refer [evaluate-script tinkerpop->clj]]
            [clojure.data.json   :as json]
            [clojure.java.io     :refer [reader]]))


(defn home-page [req]
  "Hello, Speedster!")

(defn query-page [{:keys [body]}]
  (if body 
    (with-open [rdr (reader body :encoding "UTF-8")]
      (let [results (->  rdr
                         line-seq
                         (#(apply str %))
                         (or "")
                         (json/read-str :key-fn keyword)
                         evaluate-script)]
        (json/write-str
         (if (instance? Exception results)
           {:results "ERROR"
            :sucess false}
           {:results (tinkerpop->clj results)
            :sucess true}))))
    (json/write-str
     {:results "ERROR: NO BODY"
      :sucess false})))

(defroutes all-routes
  (GET "/"         [] home-page)
  (POST "/execute" [] query-page)
  (not-found "<p>Page not found.</p>"))

(defn -main [& args]
  (run-server (site #'all-routes) {:port 8080})
  (println "Server running"))






















