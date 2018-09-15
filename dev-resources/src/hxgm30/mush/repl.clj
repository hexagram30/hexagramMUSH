(ns hxgm30.mush.repl
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]
    [clojure.tools.namespace.repl :as repl]
    [clojusc.system-manager.core :refer :all]
    [clojusc.twig :as logger]
    [com.stuartsierra.component :as component]
    [hxgm30.common.util :as util]
    [hxgm30.db.plugin.component :as backend]
    [hxgm30.db.plugin.util :as plugin-util]
    [hxgm30.mush.components.config :as config]
    [hxgm30.mush.components.core]
    [trifl.java :refer [show-methods]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Initial Setup & Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- call-if-no-error
  [func sys & args]
  (if (:error sys)
    sys
    (apply func (cons sys args))))

(defn -load-backend-specific-dev
  [system]
  (condp = (backend/backend-plugin system)
    :redis (load "/hxgm30/graphdb/plugin/redis/dev")
    :skip-load))

(defn load-backend-specific-dev
  []
  (call-if-no-error -load-backend-specific-dev (system)))

(def setup-options {
  :init 'hxgm30.mush.components.core/init
  :after-refresh 'hxgm30.mush.repl/init-and-startup
  :throw-errors false})

(defn init
  "This is used to set the options and any other global data.

  This is defined in a function for re-use. For instance, when a REPL is
  reloaded, the options will be lost and need to be re-applied."
  []
  (logger/set-level! '[hxgm30] :debug)
  (setup-manager setup-options)
  (load-backend-specific-dev))

(defn init-and-startup
  "This is used as the 'after-refresh' function by the REPL tools library.
  Not only do the options (and other global operations) need to be re-applied,
  the system also needs to be started up, once these options have be set up."
  []
  (init)
  (startup))

;; It is not always desired that a system be started up upon REPL loading.
;; Thus, we set the options and perform any global operations with init,
;; and let the user determine when then want to bring up (a potentially
;; computationally intensive) system.
(init)

(defn banner
  []
  (println (slurp (io/resource "text/banner.txt")))
  :ok)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Data Support   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-backend
  []
  (call-if-no-error backend/backend-plugin (system)))

(defn conn
  []
  (call-if-no-error backend/db-conn (system)))

(defn factory
  []
  (call-if-no-error backend/factory (system)))

(comment
  (startup)
  (db* 'vertices)
  (db* 'get-vertex "node:820fd255-039c-4f47-aa1a-fcd5634ff008")
  (def cave (db* 'add-vertex g {:type :room :name "A cave" :description "You are in a dark cave."}))
  (def tunnel (db* 'add-vertex g {:type :room :name "A tunnel" :description "You are in a long, dark tunnel."}))
  )
