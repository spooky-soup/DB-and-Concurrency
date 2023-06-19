(ns task1)
(require '[clojure.string :as str])

(defn my-concat [a b]
  (if (not (str/starts-with? a b)) (str b a)))

(defn add-letters [word alph]
  (map #(my-concat word %) alph)
  )

(defn step [words alph]
  (reduce concat (map #(add-letters % alph) (filter boolean words)))
  )

(defn permute [alph len]
  (filter boolean (reduce step [""] (repeat len alph)))
  )

(defn -main [& args]
  (prn (permute '("1" "2" "3") 4))
  )



;Given an alphabet in form of a list containing 1-character strings and a number N. Define a function that
;returns all the possible strings of length N based on this alphabet and containing no equal subsequent
;characters.
;Use map/reduce/filter/remove operations and basic operations for lists such as str, cons, .concat, etc.
;No recursion, generators or advanced functions such as flatten!
;Example: for the alphabet ("а" "b " "c") and N=2 the result bust be ("ab" "ac" "ba" "bc" "ca" "cb") up to
;permutation.