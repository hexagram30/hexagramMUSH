(ns hexagram30.mush.components.core
  (:require
    [com.stuartsierra.component :as component]
    [hexagram30.mush.components.config :as config]
    [hexagram30.mush.components.database :as database]
    [hexagram30.mush.components.httpd :as httpd]
    [hexagram30.mush.components.logging :as logging]
    [hexagram30.mush.components.terminal :as terminal]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Common Configuration Components   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def cfg
  {:config (config/create-component)})

(def log
  {:logging (component/using
             (logging/create-component)
             [:config])})

(def db
  {:database (component/using
         (database/create-component)
         [:config :logging])})

(def httpd
  {:httpd (component/using
           (httpd/create-component)
           [:config :logging :database])})

(def terminal
  {:terminal (component/using
           (terminal/create-component)
           [:config :logging :database])})

(def common
  (merge cfg log db))

(def terminal-only
  (merge common terminal))

(def with-web
  (merge terminal-only httpd))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Initializations   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn initialize-bare-bones
  []
  (component/map->SystemMap common))

(defn initialize-with-terminal
  []
  (component/map->SystemMap terminal-only))

(defn initialize-with-web
  []
  (component/map->SystemMap with-web))

(def init-lookup
  {:basic #'initialize-bare-bones
   :terminal-only initialize-with-terminal
   :with-web #'initialize-with-web})

(defn init
  ([]
    (init :terminal-only))
  ([mode]
    ((mode init-lookup))))
