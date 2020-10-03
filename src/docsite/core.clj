(ns docsite.core
  (:require [docsite.templates.index :as index-page]
            [docsite.templates.article :as article-page]
            [markdown.core :refer [md-to-html-string-with-meta]]
            [clojure.string :as string]
            [docsite.articles :refer [read-articles-from-disc]]
            [docsite.config :refer [public-articles-path public-path]]
            [docsite.import :refer [hatenablog-import]])
  (:gen-class))

(defn compile-article-page [article]
  (let [html (:html article)
        title (:title article)
        name (:name article)]
    (spit (str public-articles-path "/" name ".html")
          (article-page/page html title))))

(defn compile-all-article-page []
  (for [article (read-articles-from-disc)]
    (do
      (compile-article-page article)
      )))

(defn compile-index-page [articles]
  (spit (str public-path "/index.html")
        (index-page/page articles)))

(defn static-compile []
  (let [article-collection (sort-by :name
                                    (into (read-articles-from-disc)
                                          (hatenablog-import "resources/arakaji.hatenablog.com.export.txt")))]
    (doall (map compile-article-page article-collection))
    (do (compile-index-page (reverse article-collection)))
  ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (do
    (println "----- Compile Pages -----")
    (static-compile)
    (println "----- Done -----")
    ))
