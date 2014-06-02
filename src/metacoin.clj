;;;; Copyright 2013 Aviad Reich.
;;;; The use and distribution terms for this software are covered by
;;;; the Apache License, Version 2.0
;;;; (http://www.apache.org/licenses/LICENSE-2.0.txt), which can be
;;;; found in the file LICENSE at the root of this distribution. By
;;;; using this software in any fashion, you are agreeing to be bound
;;;; by the terms of this license. You must not remove this notice, or
;;;; any other, from this software.

(ns metacoin
  (:require [clojure.data.json :as json])
  (:require [org.httpkit.client :as http])
  (:require [clojure.java.io :as jio :refer (reader)])
  (:require [metacoin.config :refer (read-local-config)])
  (:import java.io.StringReader))

(set! *warn-on-reflection* true)

(def ^java.util.concurrent.atomic.AtomicInteger id-num
  (java.util.concurrent.atomic.AtomicInteger.))

(def not-nil? (comp not nil?))

(defn valid-rpc-config?
  [{:keys [rpcuser rpcpassword rpchost rpcport testnet]}]
  (and (string? rpcuser)
       (string? rpcpassword)
       (string? rpchost)
       (number? rpcport)
       (not-nil? testnet)))

(defn complete-rpc-config
  [coin config]
  (if-not (valid-rpc-config? config)
    (let [new-cfg (merge (read-local-config
                          (:coin config coin))
                         config)]
      (assert (valid-rpc-config? new-cfg)
              "Failed to build RPC configuration!")
      new-cfg)
    config))

(defn rpc-call
  "Perform rpc call of method with params, according to config.
   Might throw an exception in case of a non-bitcoin error
   (i.e a connection is refused)."
  [config method params]
  (let [resp
        @(http/post
          (str (config :rpchost) ":" (config :rpcport))
          {:basic-auth [(config :rpcuser) (config :rpcpassword)],
           :headers {"Content-Type" "application/json; charset=utf-8"},
           :body (json/write-str {"version" "1.1",
                                  "params" params,
                                  "method" method,
                                  "id" (.incrementAndGet id-num)})})]
    (if-let [err (:error resp)]
      (throw err)
      (if (= 200 (:status resp))
        (-> resp :body StringReader. (json/read :bigdec true) (get "result"))
        (-> resp :body json/read-str (get "error"))))))

(defn do-rpc
  [coin name doc args premap]
  (let [args-form `[& {:keys [~'config ~@args]
                        :or  {~'config {}}}]
        premap (merge-with (comp vec concat)
                           {:pre `[(map? ~'config)]}
                           premap)]
    `(defn ~name ~doc ~args-form
       ~premap
       (let [params# (vec (take-while not-nil? ~args))
             ~'config (complete-rpc-config ~coin ~'config)]
         (rpc-call ~'config ~(str name) params#)))))

(defmacro defrpc
  "Create a method for rpc.

  Optional parameters should end with a '?'  as per the Clojure naming
  convention. Defining pretests is the way to handle required
  parameters."
  [coin name doc args & premap]
  (do-rpc coin name doc args (first premap)))
