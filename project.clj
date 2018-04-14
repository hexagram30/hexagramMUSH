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
    [clojusc/trifl "0.2.0"]
    [clojusc/twig "0.3.2"]
    [com.stuartsierra/component "0.3.2"]
    [hexagram30/common "0.1.0-SNAPSHOT"]
    [hexagram30/graphdb "0.1.0-SNAPSHOT"]
    [hexagram30/graphdb-redis-plugin "0.1.0-SNAPSHOT"]
    [hexagram30/terminal "0.1.0-SNAPSHOT"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/tools.nrepl "0.2.12"]]
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
        :init-ns hxgm30.mush.dev
        :prompt ~get-prompt
        :init ~(println (get-banner))}}
    :lint {
      :source-paths ^:replace ["src"]
      :test-paths ^:replace []
      :plugins [
        [jonase/eastwood "0.2.5"]
        [lein-ancient "0.6.15"]
        [lein-bikeshed "0.5.1"]
        [lein-kibit "0.1.6"]
        [venantius/yagni "0.1.4"]]}
    :test {
      :plugins [[lein-ltest "0.3.0"]]}
      :server {
        :jvm-opts ["-XX:MaxDirectMemorySize=512g"]
        :main hxgm30.graphdb.server}}
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
    "extract-docker-compose"
      ["run" "-m" "hxgm30.mush.cli.extract" "docker/docker-compose-redis.yml"]
    "start-db" ["do"
      ["extract-docker-compose"]
      ["shell" "docker-compose"
                 "-f" "/tmp/docker/docker-compose-redis.yml"
                 "up" "--remove-orphans"]]
    "stop-db" ["do"
      ["extract-docker-compose"]
      ["shell" "docker-compose"
                 "-f" "/tmp/docker/docker-compose-redis.yml"
                 "down"]]
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
      ;["eastwood"]
      ]
    "ltest" ["with-profile" "+test" "ltest"]
    })
