(ns docsite.articles
  (:require [clojure.string :as string]
            [docsite.config :refer [articles-directory]]
            [markdown.core :refer [md-to-html-string-with-meta]]))

(defrecord Article [file name path title created-at html])

(defn make-article [file]
  (let [markdown (read-markdown {:path (.getPath file)})]
    (->Article file
               (string/replace (.getName file) #"\.md$" "")
               (.getPath file)
               (first (:title (:metadata markdown)))
               (first (:createdat (:metadata markdown)))
               (:html markdown)
               )))

(defn read-articles-from-disc []
  (map make-article (.listFiles (java.io.File. docs-path))))

(defn web-url [article]
  (str articles-directory "/" (:name article) ".html"))

(defn read-markdown [article]
  (-> (slurp (:path article))
      (md-to-html-string-with-meta)))
