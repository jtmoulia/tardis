(ns tardis.util)

(defn index-of
  "Returns the first index for which f holds true"
  [f coll]
  (loop [[head & rest] coll
         i 0]
    (if (nil? head) nil
        (if (f head) i (recur rest (inc i))))))

(defn find-first
  "Returns the first element for which f holds true"
  [f coll]
  (first (filter f coll)))
