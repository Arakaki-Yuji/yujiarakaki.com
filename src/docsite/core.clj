(ns docsite.core
  (:require [docsite.templates.index :as index-page]
            [docsite.templates.article :as article-page]
            [markdown.core :refer [md-to-html-string]])
  (:gen-class))

(def public-path "resources/public")

(def docs-path "resources/docs")

(defn compile-aritcle-pages []
  (let [md (slurp (str docs-path "/hello-world.md"))
        html (md-to-html-string md)]
    (spit (str public-path "/article-page.html")
          (article-page/page html))))

(defn compile-index-page []
  (spit (str public-path "/index-page.html") (index-page/page)))

(defn static-compile []
  (do
    (compile-index-page)
    (compile-aritcle-pages)
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
