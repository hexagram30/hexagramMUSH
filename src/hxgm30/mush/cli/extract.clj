(ns hxgm30.mush.cli.extract
  (:require
    [clojure.java.io :as io])
  (:gen-class))

(defn get-tmp-file
  [filename]
  (when filename
    (let [tmp-name (str "/tmp/" filename)]
      (io/make-parents tmp-name)
      (when-let [resource (io/resource filename)]
        (spit tmp-name (slurp resource)))
      tmp-name)))

(defn -main
  [filename]
  (get-tmp-file filename))
