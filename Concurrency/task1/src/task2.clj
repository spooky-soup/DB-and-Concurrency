(ns task2)
(use 'clojure.test)
;Define the infinite sequence of prime numbers. Use Sieve of Eratosthenes algorithm with infinite cap.
;Cover code with unit tests.

(defn prime-step [x table]
  (if (table x)
    (prime-step (inc x)
                 (reduce #(update % (+ x %2) conj %2)       ;
                         (dissoc table x)                   ;
                         (table x)))
    (lazy-seq (cons x (prime-step (inc x) (assoc table (* x x) [x]))))))

(def sieve
  (concat [2] (lazy-seq (prime-step 3 {4 [2]}))))

(defn -main [& args]
  ;(prn (nth (sieve (iterate inc 2)) 167))
  (prn (take 1 sieve))
  )

(deftest prime-tests
  (is (= 997 (last (take 168 sieve))))
  (is (= 2 (last (take 1 sieve))))
  (is (= 9973 (last (take 1229 sieve))))
  (is (= 99991 (last (take 9592 sieve))))
  (is (= 199999 (last (take 17984 sieve)))))

