(ns speedster.util)

(def new-uuid #(str (java.util.UUID/randomUUID)))

(defprotocol IUuid 
  (getUuid   [_])
  (setUuid   [_ v]))

(defprotocol IQuery
  (getScript [_])
  (getParams [_])
  (getFlags  [_])
  (setScript [_ v])
  (setParams [_ v])
  (setFlags  [_ v]))

(deftype Query [^:unsynchronized-mutable uuid
                ^:unsynchronized-mutable script
                ^:unsynchronized-mutable params
                ^:unsynchronized-mutable flags]
  IUuid
  (getUuid   [_]   uuid)
  (setUuid   [_ v] (set! uuid v))

  IQuery
  (getScript [_]   script)
  (getParams [_]   params)
  (getFlags  [_]   flags)
  (setScript [_ v] (set! script v))
  (setParams [_ v] (set! params v))
  (setFlags  [_ v] (set! flags  v)))

(defprotocol IResults
  (getResults [_])
  (setResults [_ v]))

(deftype Results [^:unsynchronized-mutable uuid
                  ^:unsynchronized-mutable results]
  IUuid
  (getUuid   [_]   uuid)
  (setUuid   [_ v] (set! uuid v))

  IResults
  (getResults  [_]   results)
  (setResults  [_ v] (set! results  v)))
