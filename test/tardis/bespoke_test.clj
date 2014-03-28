(ns tardis.bespoke_test
  (:require [midje.sweet :refer :all]
            [clj-time.core :refer [within? before? after?]]
            clj-time.periodic
            tardis.interval))

(def ref (date-time 2014 3 3))

;;; These are a first cut, and  will need to be rewritten

(facts
 "Let's catch up Saturday after tomorrow"
 (take-while
  (fn [_] true)
  (take 1 (tardis.interval/every :day-of-week :saturday (tardis.interval/tomorrow)))))

(facts
 "Let's catch up Saturday next week."
 (take-while
  (fn [d] (within? d (relative-week :next 1)))
  (tardis.interval/every :day-of-week :saturday)))

(facts
 "Let's catch up Friday this week."
 (take-while
  (fn [d] (within? d (next-week :ref ref)))
  (every :day-of-week :on :friday)))

(facts
 "Let's meet every Wednesday after next Tuesday before next month."
 (let [today (tardis.interval/enclose :day (clj-time.core/date-time 2013 3 5))
       start (tardis.interval/subsequent :day-of-week :tuesday today)]
  (take-while
   (fn [d] (and (tardis.interval/before? d (tardis.interval/get-next :month today))
                (tardis.interval/after? d start)))
   (tardis.interval/every :day-of-week :wednesday start))))
