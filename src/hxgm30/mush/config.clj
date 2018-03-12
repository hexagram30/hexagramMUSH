(ns hxgm30.mush.config
  (:require
   [hxgm30.common.file :as common]))

(def config-file "hexagram30-config/mush.edn")

(defn data
  ([]
    (data config-file))
  ([filename]
    (common/read-edn-resource filename)))
