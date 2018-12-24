; (ns hxgm30.mush.core)
(ns hxgm30.mush.core
  (:require
    [clojusc.twig :as logger]
    [com.stuartsierra.component :as component]
    [hxgm30.mush.components.core :as system]
    [taoensso.timbre :as log]
    [trifl.java :as java])
  (:gen-class))

(defn -main
  []
  (logger/set-level! '[hxgm30] :info)
  (log/info "Bringing up HexagramMUSH system ...")
  (let [system (system/init)]
    (component/start system)
    (java/add-shutdown-handler #(component/stop system)))
  (Thread/sleep 500)
  (log/info "System up; HexagramMUSH is ready to use."))
