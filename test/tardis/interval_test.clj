(ns tardis.interval_test
  (:require [midje.sweet :refer :all]
            tardis.interval
            [clj-time.core :refer [date-time years]]))

(fact
 "Test the truncate fn"
 (tardis.interval/truncate :year (date-time 2014 3 10)) =>
   (date-time 2014)
 (tardis.interval/truncate :hour (date-time 2014 3 10 6 12 0)) =>
   (date-time 2014 3 10 6))

(fact
 "Test the enclosing-interval fn"
 (tardis.interval/enclose :year (date-time 2014 3 10))
   => [(date-time 2014) (date-time 2015)])

(fact
 "Test subsequent multimethod"
 (tardis.interval/subsequent :day-of-week :saturday
                             (tardis.interval/enclose :day (date-time 2014 3 10)))
   => [(date-time 2014 3 15) (date-time 2014 3 16)]
 (tardis.interval/subsequent :day-of-week :monday
                             (tardis.interval/enclose :day (date-time 2014 3 10)))
   => [(date-time 2014 3 17) (date-time 2014 3 18)])

(fact
 "Test subsequent multimethod with month"
 (tardis.interval/subsequent2 :month :saturday
                             (tardis.interval/enclose :day (date-time 2014 3 10)))
   => [(date-time 2014 3 15) (date-time 2014 3 16)]
 (tardis.interval/subsequent :day-of-week :monday
                             (tardis.interval/enclose :day (date-time 2014 3 10)))
   => [(date-time 2014 3 17) (date-time 2014 3 18)])

(fact
 (tardis.interval/get-next :month (tardis.interval/enclose :day (date-time 2014 3 10))) =>
   [(date-time 2014 4) (date-time 2014 5)])

(fact
 "Test the every multimethod"
 (take 2 (tardis.interval/every :day-of-week :saturday
                                (tardis.interval/enclose :day (date-time 2014 3 10))))
   => [[(date-time 2014 3 15) (date-time 2014 3 16)]
       [(date-time 2014 3 22) (date-time 2014 3 23)]])
