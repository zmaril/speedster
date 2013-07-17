(ns speedster.graph
  (:import  [javax.script ScriptEngineManager]
            [com.tinkerpop.blueprints.impls.tg TinkerGraphFactory]))

(def engine
  (.getEngineByName (ScriptEngineManager.) "gremlin-groovy"))

(def graph
  (TinkerGraphFactory/createTinkerGraph))

(defn evaluate-script
  "Takes in an Query instance and runs the query script aganist the
  graph engine and returns the results. If there is an error, it
  returns the error object."
  ([script] (evaluate-script script nil))
  ([script params]
     (let [bindings (.createBindings engine)]
       (doseq [[k v] (merge {:g graph} params)]
         (.put bindings (name k) v))
       (try       
         (.eval engine script bindings)
         (catch Exception e e)))))

;;Clear the tinkergraph of all objects. 
(evaluate-script "g.E.remove(); g.V.remove()")


