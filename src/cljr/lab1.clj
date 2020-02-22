(ns cljr.core
  (:gen-class))

;; Напишите функцию, возвращающую ленивую последовательность элементов путем взятия элементов из другой последовательности и группировки их в вектор если эти собранные элементы набирают сумму, большую определенного значения по заданной функции-критерию.

;; UPD Код собирал вектор из элементов по достижению определенной суммы и возвращал его. Сделал чтобы он продолжал собирать векторы до конца последовательности.

(defn func [] (+ 20 70))

(defn sum-seq
  [init-seq]
  (let [
    all-sums
      (map-indexed vector (reductions + init-seq))
    sum-index
      (some (fn [el] (when (> (get el 1) (func)) (get el 0))) all-sums)
    ]
    ;; (println "sum-index = " sum-index)
    ;; (println "count = " (count init-seq))
    ;; (println "init-seq = " init-seq)
    (if (and (some? sum-index) (> (count init-seq) 0))
      (lazy-seq (cons (vec (take (+ sum-index 1) init-seq)) (sum-seq (drop (+ sum-index 1) init-seq))))
      nil)))


(defn -main
  [& args]
  
  (sum-seq (take 50 (range)))
    

)