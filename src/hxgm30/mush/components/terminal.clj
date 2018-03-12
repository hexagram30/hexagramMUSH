(ns hxgm30.mush.components.terminal
  (:require
    [com.stuartsierra.component :as component]
    [hxgm30.mush.components.config :as config]
    [hxgm30.terminal.telnet.server :as telnet]
    [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Telnet Server Component API   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Lifecycle Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord Telnet [server])

(defn start
  [this]
  (log/info "Starting telnet component ...")
  (let [port (config/telnet-port this)
        ssl? false
        server (telnet/init)]
    (telnet/start server port ssl?)
    (log/debugf "Telnet server is listening on port %s" port)
    (log/debug "Started telnet component.")
    (assoc this :server server)))

(defn stop
  [this]
  (log/info "Stopping telnet component ...")
  (if-let [server (:server this)]
    (telnet/stop server))
  (log/debug "Started telnet component.")
  (assoc this :server nil))

(def lifecycle-behaviour
  {:start start
   :stop stop})

(extend Telnet
  component/Lifecycle
  lifecycle-behaviour)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Constructor   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-component
  ""
  []
  (map->Telnet {}))
