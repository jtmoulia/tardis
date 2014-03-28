(ns tardis.interval
  (:require clj-time.core
            tardis.util
            [clojure.math.numeric-tower :as math]))


;;; Functions for manipulating time intervals

;;; I might end up regretting this, but I broke with clj-time tradition:
;;; these time intervals use [start end] joda DateTime vectors instead of joda
;;; Intervals, and plus/minus args have been flipped. I did this to make
;;; functional idioms (map!) ever so slightly simpler.


(defn as-vec
  "Convert an interval to a date-time tuple"
  [interval]
  [(clj-time.core/start interval) (clj-time.core/end interval)])

(def ^:private date-components
  "The components which make up a joda.DateTime"
  [[:year clj-time.core/year]
   [:month clj-time.core/month]
   [:day clj-time.core/day]
   [:hour clj-time.core/hour]
   [:minute clj-time.core/minute]
   [:second clj-time.core/second]
   [:milli clj-time.core/milli]])

(defn truncate
  "Truncate a date or list of dates to the given accuracy"
  [to date]
  (let [index (tardis.util/index-of #(= to (first %)) date-components)]
       (apply clj-time.core/date-time (->> date-components
                                           (take (inc index))
                                           (map #((second %) date))))))

;;; Date Manipulation
(def ^:private date-periods
  {:year clj-time.core/years
   :month clj-time.core/months
   :day clj-time.core/days
   :hour clj-time.core/hours
   :minute clj-time.core/minutes
   :second clj-time.core/seconds
   :milli clj-time.core/millis})

(defmulti enclose "Returns the enclosing intervals about a date or seq of dates"
  (fn [to _] to))
(defmethod enclose :week
  [to date]
  (let [truncated (truncate to date)]
    [truncated truncated]))
(defmethod enclose :default
  [to date]
  (let [truncated (truncate to date)]
    [truncated (clj-time.core/plus truncated ((to date-periods) 1))]))

(def days-of-week
  "Map from day of week symbols to number"
  (into {} (map vector
                [:monday
                 :tuesday
                 :wednesday
                 :thursday
                 :friday
                 :saturday
                 :sunday]
                (range 1 8))))

(defn ^:private days-until-day-of-week
  "Returns the integer number of days until the day of week"
  [on [_ end]]
  (math/abs (- (clj-time.core/day-of-week end) (on days-of-week))))

(defmulti subsequent
  "For a given interval, get the subsequent value as an interval.
   This will also enclose it using a sensible interval."
  (fn [interval & _] interval))
(defmethod subsequent :day-of-week
  [_ on [end _ :as interval] & {:keys [inclusive] :or {inclusive false}}]
  (let [days-until (days-until-day-of-week on interval)
        inc-days-until (if (and (not inclusive) (= 0 days-until)) 7 days-until)]
    (enclose :day (->> inc-days-until
                       clj-time.core/days
                       (clj-time.core/plus end)))))
;; (defmethod subsequent :week
;;   [_ on [start _] & {:keys [inclusive] :or {inclusive false}}]
;;   (let [days-until (math/abs (- (clj-time.core/day-of-week start) (on days-of-week)))]
;;       (if inclusive
;;      (let ))))

(defmulti get-next
  (fn [interval & _] interval))
(defmethod get-next :default
  ([interval] (get-next interval (today)))
  ([interval date] (get-next interval 1 date))
  ([interval i [_ end]]
     (map #(clj-time.core/plus % ((interval date-periods) i)) (enclose interval end))))

(defmulti every
  "Lazy list of intervals -- works with non-fixed intervals, such as months"
  (fn [interval & _] interval))
(defmethod every :day-of-week
  ([interval on] (every interval on (today)))
  ([interval on date]
     (let [current-day-of-week (subsequent interval on date)]
       (cons current-day-of-week (lazy-seq (every interval on current-day-of-week))))))

(defn within? [this [that-start that-end]]
  "Returns true if this interval is within that interval"
  (every? (map #(clj-time.core/within? that-start that-end %) this)))

(defn after? [[this-start _] [_ that-end]]
  (or (= this-start that-end) (clj-time.core/after? this-start that-end)))

(defn before? [[_ this-end] [that-start _]]
  (clj-time.core/before? this-end that-start))

(defn today []
  "Returns the day enclosing now"
  (tardis.interval/enclose :day (clj-time.core/now)))

(defn tomorrow []
  "Returns the day enclosing tomorrow"
  (map #(-> % (clj-time.core/plus (clj-time.core/days 1))) (today)))
