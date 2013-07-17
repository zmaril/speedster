(ns speedster.disruptor
  (:import  [speedster.util Query Results])
  (:require [speedster.graph :refer [evaluate-script]]
            [phaser.disruptor       :refer [defhandler deftranslator1] :as disruptor] 
            [phaser.dsl             :as dsl]))

(def query-factory 
  (disruptor/create-event-factory 
   #(Query. "" nil nil nil)))

(def result-factory 
  (disruptor/create-event-factory 
   #(Query. nil nil)))

(defhandler journaller
  [event sequence end-of-batch?]
  (println "Journalling" sequence end-of-batch?))

(defhandler query-logic
  [event sequence end-of-batch?]
  (println "Querying" (.getScript event) sequence end-of-batch?)
  (println  (evaluate-script (.getScript event) (.getParams event))))

(deftranslator1 translator
  [event sequence params]
  (println "Translating" sequence params)
  (.setUuid   event (params :uuid ""))
  (.setScript event (params :script ""))
  (.setParams event (params :params {}))
  (.setFlags  event (params :flags  {}))
  (println "Translated" (.getUuid event) (.getScript event) (.getParams event) (.getFlags event)))

(defn wire-up-disruptor 
  [exec respond]
  (let [d (dsl/create-disruptor query-factory 1024 exec)]
    ;;Use another disruptor here for responses! 
    (-> d
        (dsl/handle-events-with journaller)
        (dsl/then query-logic))
    (let [rb        (dsl/start d)]
      [d (disruptor/create-event-publisher rb translator)])))

