(ns speedster.core
  (:import  [org.vertx.java.core Vertx Handler])
  (:require [speedster.disruptor :as disruptor]
            [speedster.util      :as util]
            [phaser.dsl          :as dsl]
            [phaser.disruptor    :refer [defhandler]]
            [clojure.data.json   :as json]))

(def requests (atom {}))

;;TODO thread things through correctly
(defn reify-handler [f]
  (reify Handler
    (handle [this v] (f v))))

(def ^{:dynamic true} *publish*
  nil)

(defhandler respond
  [event sequence end-of-batch?]
  (let [req (swap! requests disj (.getUuid event))]
    (.. req
        response 
        (putHeader "content-type" "text/plain")       
        (end (str (count (.getResults event)))))))

(defn submit-query [req]  
  (.bodyHandler 
   req
   (reify-handler (fn [data]
                    (let [uuid (util/new-uuid)]
                      (swap! requests conj [uuid req])
                      (-> (.getString data 0 (.length data))
                          (json/read-str :key-fn keyword)
                          (assoc :uuid uuid)
                          *publish*))))))

(defn index-page [req]
  (.. req
      response 
      (putHeader "content-type" "text/plain")
      (end "Hello World!\n")))

(defn error-page [req]
  (.. req
      response 
      (putHeader "content-type" "text/plain")
      (end "No such page!\n")))

(defn handle-request 
  [req]
  (case [(.method req) (.path req)]
    ["GET" "/"]  (index-page req)
    ["POST" "/execute"] (submit-query req)
    (error-page req)))

(defn -main 
  []
  (let [exec (java.util.concurrent.Executors/newCachedThreadPool)
        [d publisher] (disruptor/wire-up-disruptor exec respond)
        handle (reify-handler handle-request)]
    (alter-var-root (var *publish*) (fn [_] publisher))
    (.. Vertx newVertx
        createHttpServer
        (requestHandler handle)
        (listen 8080))
    (println "Server started")))
