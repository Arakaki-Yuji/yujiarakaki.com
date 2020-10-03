(ns docsite.templates.utils.article
  (:import [java.time.format DateTimeFormatter]))

(def created-at-formatter (DateTimeFormatter/ofPattern "yyyy/MM/dd"))

(defn created-at [article]
  (.format (:created-at article)
           created-at-formatter))
