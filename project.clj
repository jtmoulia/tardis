(defproject tardis "0.1.0-SNAPSHOT"
  :description "Time and Relative Dimension in Space"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-time "0.6.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/core.typed "0.2.38"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]
                                  [org.clojure/tools.trace "0.7.8"]]}})
