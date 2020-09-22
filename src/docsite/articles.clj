(ns docsite.articles
  (:require [clojure.string :as string]))

(def docs-path "resources/docs")

(defrecord Article [file name path])

(defn make-article [file]
  (->Article file
             (string/replace (.getName file) #"\.md$" "")
             (.getPath file)))

(defn read-articles-from-disc []
  (map make-article (.listFiles (java.io.File. docs-path))))
