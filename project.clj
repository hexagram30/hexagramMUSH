(defproject hexagram30/hexagram-mush "0.1.0-SNAPSHOT"
  :description "A JVM (Clojure) MUSH Platform, built on a graph database"
  :url "https://github.com/hexagram30/hexagramMUSH"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
    [clojusc/twig "0.3.2"]
    [com.stuartsierra/component "0.3.2"]
    [hexagram30/common "0.1.0-SNAPSHOT"]
    [hexagram30/graphdb "0.1.0-SNAPSHOT"]
    [hexagram30/terminal "0.1.0-SNAPSHOT"]
    [org.clojure/clojure "1.8.0"]]
  :main hxgm30.mush.core)
