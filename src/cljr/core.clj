;; Написать функцию принимающую на вход канал с задачами и массив агентов, и распределяющую задачи по агентам, результаты выполнения задач агентами записывать в выходной канал.

(ns cljr.core
  (:require [clojure.core.async
    :as a
    :refer [>!! <!! chan go-loop]])
  (:gen-class))

(defn do-task
  [init-value task callback]
    (let [result (task)]
    ;; (println "Task done") 
    (callback result)))

(defn agents-do-tasks
  [tasks agents output]
  (let [agents-chan (chan (count agents))]
    (doseq [ag agents] (>!! agents-chan ag))
    (go-loop []
      (when-let [task (<!! tasks)]
        (let [ag (<!! agents-chan)]
          (send ag do-task task (fn [result] (>!! output result) (>!! agents-chan ag)))))
      (recur))))
    

(defn -main
  [& args]
  
  (let [tasks (chan 10)
        results (chan 10)
        agents (list (agent 0) (agent 1))]

    (>!! tasks #(+ 1 2))
    (>!! tasks #(* 3 4))
    (>!! tasks #(/ 5 6))
    (>!! tasks #(mod 7 8))
  
    (agents-do-tasks tasks agents results)
    (println)
    
    (go-loop []
       (let [x (<!! results)]
         (println "Got a value in this loop:" x))
       (recur))
))
