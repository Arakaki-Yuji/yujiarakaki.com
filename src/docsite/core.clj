(ns docsite.core
  (:require [docsite.templates.index :as index-page]
            [docsite.templates.article :as article-page]
            [markdown.core :refer [md-to-html-string]]
            [clojure.string :as string]
            [docsite.articles :refer [read-articles-from-disc]])
  (:gen-class))

(def public-path "resources/public")

(def public-articles-path (str public-path "/articles"))

(defn compile-article-page [doc-path page-name]
  (let [md (slurp doc-path)
        html (md-to-html-string md)]
    (spit (str public-articles-path "/" page-name ".html")
          (article-page/page html))))

(defn compile-all-article-page []
  (for [doc (read-articles-from-disc)]
    (do
      (compile-article-page (:path doc) (:name doc))
      )))

(defn compile-index-page []
  (spit (str public-path "/index.html") (index-page/page)))

(defn static-compile []
  (do
    (compile-index-page)
    (compile-all-article-page)
    )
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (do
    (println "----- Compile Pages -----")
    (static-compile)
    (println "----- Done -----")
    ))
