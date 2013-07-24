(ns speedster.graph
  (:import  [javax.script ScriptEngineManager]
            [com.tinkerpop.blueprints.impls.tg TinkerGraphFactory]
            [com.tinkerpop.blueprints Vertex Edge Graph Direction]
            [com.tinkerpop.pipes.util Pipeline]
            [com.tinkerpop.blueprints.util ElementHelper]))


(defn tinkerpop->clj 
  "Takes in the results of a Gremlin query and converts it into
  data structures that can later be turned into json."
  [o]
  (cond 
   (instance? Vertex o)
   (assoc (into {} (ElementHelper/getProperties o))
     "_id"   (.getId o)
     "_type" "vertex")

   (instance? Edge o) 
   (assoc (into {} (ElementHelper/getProperties o))
     "_id"    (.getId o)
     "_type"  "edge"
     "_label" (.getLabel o)
     "_inV"   (.getId (.getVertex o Direction/IN) )
     "_outV"  (.getId (.getVertex o Direction/OUT)))

   (instance? Graph o)
   (.toString o) ;;TODO: make this more descriptive

   (instance? Pipeline o)
   (.toString o)

   (sequential? o)
   (map tinkerpop->clj o)

   (instance? java.util.ArrayList o)
   (map tinkerpop->clj o)

   (instance? java.util.HashMap o)
   (into {} o)
   :else  o))


(def engine
  (let [engine (.getEngineByName (ScriptEngineManager.)
                                 "gremlin-groovy")
        bindings (.createBindings engine)]
    (.put bindings "g" (TinkerGraphFactory/createTinkerGraph))
    (.setBindings (.getContext engine) bindings 200)
    (.eval engine "g.E.remove(); g.V.remove()")
    engine))

(defn evaluate-script
  "Runs the script aganist the graph engine with the given parameters
  and returns the results. If there is an error, it returns the error
  object."
  ([{:keys [script params] :or {script "" params {}}}]
     (locking engine
       (let [bindings (.createBindings engine)]
         (doseq [[k v] params]
           (.put bindings (name k) v))
         (try 
           (.eval engine script bindings)
         (catch Exception e e))))))





