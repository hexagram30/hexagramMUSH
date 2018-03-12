(ns hxgm30.mush.components.httpd
  (:require
    [com.stuartsierra.component :as component]
    ;[hxgm30.http.rest.app :as rest-api]
    [hxgm30.mush.components.config :as config]
    ;[org.httpkit.server :as server]
    [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   HTTP Server Component API   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Lifecycle Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord HTTPD [])

(defn start
  [this]
  (log/info "Starting httpd component ...")
  ; (let [port (config/http-port this)
  ;       server (server/run-server (rest-api/app this) {:port port})]
  ;   (log/debugf "HTTPD is listening on port %s" port)
  ;   (log/debug "Started httpd component.")
  ;   (assoc this :server server)))
  )

(defn stop
  [this]
  (log/info "Stopping httpd component ...")
  ; (if-let [server (:server this)]
  ;   (server))
  ; (assoc this :server nil))
  )

(def lifecycle-behaviour
  {:start start
   :stop stop})

(extend HTTPD
  component/Lifecycle
  lifecycle-behaviour)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Constructor   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-component
  ""
  []
  (map->HTTPD {}))
