(ns hxgm30.mush.components.core
  (:require
    [com.stuartsierra.component :as component]
    [hxgm30.graphdb.plugin.backend :as db-backend]
    [hxgm30.mush.components.config :as config]
    [hxgm30.mush.components.httpd :as httpd]
    [hxgm30.mush.components.logging :as logging]
    [hxgm30.mush.components.nrepl :as nrepl]
    [hxgm30.mush.components.terminal :as terminal]
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
           [:config :logging :backend])})

(def terminal
  {:terminal (component/using
           (terminal/create-component)
           [:config :logging :backend])})

(def nrepl
  {:nrepl (component/using
           (nrepl/create-component)
           [:config :logging :backend :terminal])})

(defn common
  [cfg-data]
  (merge (cfg cfg-data)
         log
         (db cfg-data)))

(defn with-terminal-only
  [cfg-data]
  (merge (common cfg-data)
         terminal
         nrepl))

(defn with-web
  [cfg-data]
  (merge (common cfg-data)
         terminal
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
