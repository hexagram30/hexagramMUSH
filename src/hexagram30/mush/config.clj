(ns hexagram30.mush.config
  (:require
   [hexagram30.common.file :as common]))

(def config-file "hexagram30-config/mush.edn")

(defn data
  ([]
    (data config-file))
  ([filename]
    (common/read-edn-resource filename)))
