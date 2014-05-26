;;;; Copyright 2013 Aviad Reich.
;;;; The use and distribution terms for this software are covered by
;;;; the Apache License, Version 2.0
;;;; (http://www.apache.org/licenses/LICENSE-2.0.txt), which can be
;;;; found in the file LICENSE at the root of this distribution. By
;;;; using this software in any fashion, you are agreeing to be bound
;;;; by the terms of this license. You must not remove this notice, or
;;;; any other, from this software.

(ns metacoin.config
  (:require [clojure.java.io :as jio])
  (:require [clojure.string :refer [split capitalize]]))

(set! *warn-on-reflection* true)

(defn- read-prop-val
  "*Safely* Read a string parsed from the properties file, and parse
  it as: Integer, Boolean, String. Otherwise, throw an exception."
  [inp]
  (try (Long/parseLong inp)
       (catch NumberFormatException _
         (if (contains? #{"true" "false"} inp)
           (Boolean/parseBoolean inp)
           inp))))

(defn default-config-file
  "Return the full path (as a vector of strings) to the default *coin.conf
   file, by OS (default Linux). This is in accordance with
   https://en.bitcoin.it/wiki/Running_Bitcoin#Bitcoin.conf_Configuration_File"
  [coin]
  (let [nix-data-dir #(System/getProperty "user.home")
        win-data-dir #(System/getenv "AppData")
        os-name      (System/getProperty "os.name")]
    (->> 
         (case (first (split os-name #"\s"))
           "Mac"     [(nix-data-dir) 
                      "Library" 
                      "Application Support" 
                      (capitalize coin) 
                      (str coin ".conf")]
           "Windows" [(win-data-dir)
                      (capitalize coin)
                      (str coin ".conf")]
           ;; "Linux" is the default
           [(nix-data-dir)
            (str "." coin)
            (str coin ".conf")])
         (apply jio/file)
         str)))

;; Straight from http://stackoverflow.com/questions/7777882/loading-configuration-file-in-clojure-as-data-structure
(defn parse-config [file-name]
  "read the file according to the given "
  (let [config
        (with-open [reader (jio/reader file-name)]
          (let [props (java.util.Properties.)]
            (.load props reader)
            (into {} (for [[k v] props] [(keyword k) (read-prop-val v)]))))
        testnet (and (integer? (:testnet config))
                     (> (:testnet config) 0))]
    ;; add default values
    (merge {:rpcport (if testnet 18332 8332),
            :rpchost "http://127.0.0.1"}
           (assoc config :testnet testnet))))

(defn read-local-config
  "Return a Map of properties from the given file, or from the default
   configuration file"
  ([]
     (read-local-config 
      (default-config-file)))

  ([file-or-coin-name] 
     (if (re-matches #".*coin" file-or-coin-name)
       (parse-config
        (default-config-file
          file-or-coin-name))
       (parse-config file-or-coin-name))))
