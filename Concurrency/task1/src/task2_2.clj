(ns task2_2)
(def sieve
  (concat
    [2]
    (lazy-seq
      (let [prime-inner
            (fn prime-inner [x table]
              (if (table x)
                (prime-inner (inc x)
                             (reduce #(update % (+ x %2) conj %2)
                                     (dissoc table x)
                                     (table x)))
                (lazy-seq (cons x (prime-inner (inc x) (assoc table (* x x) [x]))))))]
        (prime-inner 3 {4 [2]})))))

(defn -main [& args]
  (prn (take 10 sieve))
  )