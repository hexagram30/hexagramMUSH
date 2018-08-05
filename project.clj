(defn get-banner
  []
  (try
    (str
      (slurp "resources/text/banner.txt")
      (slurp "resources/text/loading.txt"))
    ;; If another project can't find the banner, just skip it;
    ;; this function is really only meant to be used by Dragon itself.
    (catch Exception _ "")))

(defn get-prompt
  [ns]
  (str "\u001B[35m[\u001B[34m"
       ns
       "\u001B[35m]\u001B[33m λ\u001B[m=> "))

(defproject hexagram30/hexagram-mush "0.1.0-SNAPSHOT"
  :description "A JVM (Clojure) MUSH Platform, built on a graph database"
  :url "https://github.com/hexagram30/hexagramMUSH"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
    [clojusc/trifl "0.3.0"]
    [clojusc/twig "0.3.3"]
    [com.stuartsierra/component "0.3.2"]
    [hexagram30/common "0.1.0-SNAPSHOT"]
    [hexagram30/dice "0.1.0-SNAPSHOT"]
    [hexagram30/graphdb "0.1.0-SNAPSHOT"]
    [hexagram30/language "4.2.0-SNAPSHOT"]
    [hexagram30/map "0.1.0-SNAPSHOT"]
    [hexagram30/terminal "0.1.0-SNAPSHOT"]
    [org.clojure/clojure "1.9.0"]
    [org.clojure/tools.nrepl "0.2.13"]]
  :main hxgm30.mush.core
  :aot [hxgm30.mush.core]
  :profiles {
    :precompile {
      :aot [clojure.tools.logging.impl]}
    :ubercompile {
      :aot :all}
    :dev {
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"]]
      :plugins [
        [lein-shell "0.5.0"]
        [venantius/ultra "0.5.2"]]
      :source-paths ["dev-resources/src"]
      :repl-options {
        :init-ns hxgm30.mush.repl
        :prompt ~get-prompt
        :init ~(println (get-banner))}}
    :redis-plugin {
      :jvm-opts [
        "-Dgraphdb.backend=redis"
        "-Dgraphdb.backend.subtype=graphdb"
        "-Ddb.backend=redis"
        "-Ddb.backend.subtype=db"]
      :dependencies [
        [hexagram30/redis-db-plugin "0.1.0-SNAPSHOT"]]
      :aliases {
        "read-db-cfg" ["run" "-m" "hxgm30.db.plugin.docker" "read" "compose-redis-db.yml"]
        "start-db" ["run" "-m" "hxgm30.db.plugin.docker" "up" "compose-redis-db.yml"]
        "stop-db" ["run" "-m" "hxgm30.db.plugin.docker" "down" "compose-redis-db.yml"]
        "read-graphdb-cfg" ["run" "-m" "hxgm30.db.plugin.docker" "read" "compose-redis-graphdb.yml"]
        "start-graphdb" ["run" "-m" "hxgm30.db.plugin.docker" "up" "compose-redis-graphdb.yml"]
        "stop-graphdb" ["run" "-m" "hxgm30.db.plugin.docker" "down" "compose-redis-graphdb.yml"]}}
    :lint {
      :source-paths ^:replace ["src"]
      :test-paths ^:replace []
      :plugins [
        [jonase/eastwood "0.2.9"]
        [lein-ancient "0.6.15"]
        [lein-bikeshed "0.5.1"]
        [lein-kibit "0.1.6"]
        [venantius/yagni "0.1.6"]]}
    :test {
      :plugins [[lein-ltest "0.3.0"]]
      :server {
        :jvm-opts ["-XX:MaxDirectMemorySize=512g"]
        :main hxgm30.mush.core}}}
  :aliases {
    ;; App Aliases
    "start" ["do"
      ["clean"]
      ;["ubercompile"]
      ["trampoline" "run"]]
    ;; Dev Aliases
    "repl" ["do"
      ["clean"]
      ["with-profile" "+precompile" "compile"]
      ["repl"]]
    "ubercompile" ["do"
      ["clean"]
      ["with-profile" "+precompile" "compile"]
      ["with-profile" "+ubercompile" "compile"]]
    "check-vers" ["with-profile" "+lint" "ancient" "check" ":all"]
    "check-jars" ["with-profile" "+lint" "do"
      ["deps" ":tree"]
      ["deps" ":plugin-tree"]]
    "check-deps" ["do"
      ["check-jars"]
      ["check-vers"]]
    "kibit" ["with-profile" "+lint" "kibit"]
    "eastwood" ["with-profile" "+lint" "eastwood" "{:namespaces [:source-paths]}"]
    "lint" ["do"
      ["kibit"]
      ["eastwood"]]
    "ltest" ["with-profile" "+test" "ltest"]
    "ltest-clean" ["do"
      ["clean"]
      ["ltest"]]
    "build" ["do"
      ["clean"]
      ["check-vers"]
      ["lint"]
      ["ltest" ":all"]
      ["uberjar"]]
    "start-db" ["do"
      ["clean"]
      ["with-profile" "+server" "run"]]})
