(ns task5)

(def counter (atom 0 :validator #(>= % 0)))

(defn philosopher [number left-fork right-fork think-dur eat-dur iter]
  (let [think (fn [n] (dosync
                (println (str n " : thinking"))
                (Thread/sleep think-dur)))
        eat (fn [n] (dosync
                      (swap! counter inc)
                      (alter left-fork inc)
                      (alter right-fork inc)
                      (println (str n " : eating"))
                      (Thread/sleep eat-dur)
                      (swap! counter dec))
              ;(println  (str "| restarts: " (deref counter)))
              )]
    (new Thread (fn [] (reduce (fn [_ _] (do
                            (think number)
                            (eat number))) '()
                               (range iter))))
    )
  )

(defn dining-problem [n think-dur eat-dur iter]
  (let [forks (map ref (repeat n 0))
        philosophers (map #(philosopher % (nth forks %) (nth forks (mod (inc %) n)) think-dur eat-dur iter) (range n))
        ]
    (do
      (doall (map #(.start %) philosophers))
      (doall (map #(.join %) philosophers))))
  )

(defn -main [& args]
  (time (dining-problem 5 20 30 20))
  (println "Restarts: " (deref counter)))
