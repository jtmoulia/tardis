(ns tardis.util_test
  (:require [midje.sweet :refer :all]
            tardis.util))

(fact
 "Test the index-of fn"
 (tardis.util/index-of #(= :a (first %)) [[:a 1] [:b 2] [:c 3]]) => 0
 (tardis.util/index-of #(= :b (first %)) [[:a 1] [:b 2] [:c 3]]) => 1
 (tardis.util/index-of #(= :c (first %)) [[:a 1] [:b 2] [:c 3]]) => 2
 (tardis.util/index-of #(= :d (first %)) [[:a 1] [:b 2] [:c 3]]) => nil)
