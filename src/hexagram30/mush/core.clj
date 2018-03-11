(ns hexagram30.mush.core
  (:require
    [clojusc.twig :as logger]
    [com.stuartsierra.component :as component]
    [hexagram30.mush.components.core :as system]))

(defn -main
  []
  (logger/set-level! '[hexagram30] :info)
  (component/start (system/init))
  (.join (Thread/currentThread)))
