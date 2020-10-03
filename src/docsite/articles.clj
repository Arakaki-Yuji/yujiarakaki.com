(ns docsite.articles
  (:require [clojure.string :as string]
            [docsite.config :refer [articles-directory docs-path]]
            [markdown.core :refer [md-to-html-string-with-meta]])
  (:import [java.time LocalDate]
           [java.time.format DateTimeFormatter]))

(def date-formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd"))

(defn read-markdown [article]
  (-> (slurp (:path article))
      (md-to-html-string-with-meta)))

(defrecord Article [name title created-at html])

(defn make-article [name title created-at html]
  (->Article name title created-at html))

(defn file->article [file]
  (let [markdown (read-markdown {:path (.getPath file)})]
    (make-article (str (first (:createdat (:metadata markdown)))
                       "-"
                       (string/replace (.getName file) #"\.md$" ""))
                  (first (:title (:metadata markdown)))
                  (LocalDate/parse (first (:createdat (:metadata markdown)))
                                   date-formatter)
                  (:html markdown)
                  )))

(defn read-articles-from-disc []
  (map file->article (.listFiles (java.io.File. docs-path))))

(defn web-url [article]
  (str articles-directory "/" (:name article) ".html"))
