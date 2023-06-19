(ns task3_test
  (:require [clojure.test :refer :all]
            [task3 :refer :all]))



(deftest parallel-filter-test
(is (= (filter even? (range 0)) (pfilter even? (range 0)))))

(defn -main [& args]
  (prn (init-pfilter even? (range 20) 2)))