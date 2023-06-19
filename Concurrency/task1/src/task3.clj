(ns task3
  (:require [clojure.test :refer :all]
            [task3 :refer :all]))
;Implement a parallel variant of filter using futures. Each future must process a block of elements, not just a
;single element. The input sequence could be either finite or infinite. Thus, the implemented filter must
;possess both laziness and performance improvement by utilizing parallelism.
;Cover code with unit tests.
;Demonstrate the efficiency using time.

(defn future-filter [pred seq]
  (future (doall (filter pred seq))))

(defn pfilter [pred parts threads]
  (let [res (map #(future-filter pred %) (partition-all (/ (count (first parts)) threads) (first parts)))]
    (if (empty? parts)
      (list)
      (concat (flatten (map deref (doall res)))
              (lazy-seq (pfilter pred (rest parts) threads)))
      )))

(defn init-pfilter [pred coll threads]
  (pfilter pred (partition-all 100 coll) threads))



(defn slow-even? [x]
  (do
    (Thread/sleep 10)
    (= (mod x 2) 0)))

(defn -main [& args]
  (prn (time (doall (filter slow-even? (range 2000)))))
  (time (doall (init-pfilter slow-even? (range 2000) 2)))
  (time (doall (init-pfilter slow-even? (range 2000) 5)))
  )
