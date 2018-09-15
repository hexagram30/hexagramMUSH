(ns hxgm30.mush.components.core
  (:require
    [com.stuartsierra.component :as component]
    [hxgm30.db.plugin.backend :as db-backend]
    [hxgm30.dice.components.random :as random]
    ; [hxgm30.language.components.lang :as lang]
    [hxgm30.mush.components.config :as config]
    [hxgm30.mush.components.httpd :as httpd]
    [hxgm30.mush.components.logging :as logging]
    [hxgm30.shell.components.nrepl :as nrepl]
    [hxgm30.terminal.components.telnet :as telnet]
    [hxgm30.terminal.components.telnet-ssl :as telnet-ssl]
    [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Common Configuration Components   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn cfg
  [cfg-data]
  {:config (config/create-component cfg-data)})

(def log
  {:logging (component/using
             (logging/create-component)
             [:config])})

(def rnd
  {:random (component/using
            (random/create-component)
            [:config :logging])})

; (def language
;   {:lang (component/using
;           (lang/create-component)
;           [:config :logging :random :backend])})

(defn db
  [cfg-data]
  (log/trace "cfg-data:" cfg-data)
  (let [backend (get-in cfg-data [:backend :plugin])]
    (log/trace "backend:" backend)
    {:backend (component/using
               (db-backend/create-component backend)
               (db-backend/get-component-deps backend))}))

(def httpd
  {:httpd (component/using
           (httpd/create-component)
           [:config :logging :random :backend])})

(def telnet
  {:telnet (component/using
            (telnet/create-component)
            [:config :logging])})

(def telnet-ssl
  {:telnet-ssl (component/using
                (telnet-ssl/create-component)
                [:config :logging])})

(def nrepl
  {:nrepl (component/using
           (nrepl/create-component)
           [:config :logging :backend :telnet :telnet-ssl])})

(defn common
  [cfg-data]
  (merge (cfg cfg-data)
         rnd
         log
         (db cfg-data)))

(defn with-terminal-only
  [cfg-data]
  (merge (common cfg-data)
         telnet
         telnet-ssl
         nrepl))

(defn with-web
  [cfg-data]
  (merge (common cfg-data)
         telnet
         telnet-ssl
         nrepl
         httpd))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Initializations   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn initialize-bare-bones
  []
  (-> (config/build-config)
      common
      component/map->SystemMap))

(defn initialize-with-terminal
  []
  (-> (config/build-config)
      with-terminal-only
      component/map->SystemMap ))

(defn initialize-with-web
  []
  (-> (config/build-config)
      with-web
      component/map->SystemMap))

(def init-lookup
  {:basic #'initialize-bare-bones
   :terminal-only initialize-with-terminal
   :with-web #'initialize-with-web})

(defn init
  ([]
    (init :terminal-only))
  ([mode]
    ((mode init-lookup))))
