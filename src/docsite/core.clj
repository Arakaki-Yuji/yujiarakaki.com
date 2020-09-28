(ns docsite.core
  (:require [docsite.templates.index :as index-page]
            [docsite.templates.article :as article-page]
            [markdown.core :refer [md-to-html-string-with-meta]]
            [clojure.string :as string]
            [docsite.articles :refer [read-articles-from-disc]]
            [docsite.config :refer [public-articles-path public-path]]
            [docsite.import :refer [hatenablog-import]])
  (:gen-class))

(defn compile-article-page [doc-path page-name]
  (let [md-with-meta  (md-to-html-string-with-meta (slurp doc-path))
        html (:html md-with-meta)
        title (first (:title (:metadata md-with-meta)))]
    (spit (str public-articles-path "/" page-name ".html")
          (article-page/page html title))))

(defn compile-all-article-page []
  (for [doc (read-articles-from-disc)]
    (do
      (compile-article-page (:path doc) (:name doc))
      )))

(defn compile-index-page []
  (spit (str public-path "/index.html")
        (index-page/page (read-articles-from-disc))))

(defn static-compile []
  (do
    (hatenablog-import "resources/arakaji.hatenablog.com.export.txt")
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
