(ns hxgm30.mush.core
  (:require
    [trifl.java :as java]
    [clojusc.twig :as logger]
    [com.stuartsierra.component :as component]
    [hxgm30.mush.components.core :as system]
    [taoensso.timbre :as log]))

(defn shutdown
  [system]
  (component/stop system))

(defn -main
  []
  (logger/set-level! '[hxgm30] :info)
  (log/info "Bringing up HexagramMUSH system ...")
  (let [system (system/init)]
    (component/start system)
    (java/add-shutdown-handler #(shutdown system)))
  (Thread/sleep 500)
  (log/info "System up; HexagramMUSH is ready to use."))
