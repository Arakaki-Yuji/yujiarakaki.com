(ns docsite.templates.index
  (:require [hiccup.page :refer [html5]]
            [docsite.templates.partials.head :refer [head]]
            [docsite.templates.partials.header :refer [header]]
            [docsite.articles :refer [web-url]]
            [docsite.templates.utils.article :refer [created-at]]))


(defn page [articles]
  (html5 head
         [:body
          header
          [:main
           [:div.articleList
            (for [article articles]
              [:div.article
                [:h3.articleTitle
                 [:a {:href (web-url article)} (:title article) ]
                 ]
                [:p.articlePublishedAt (str "公開日: " (created-at article))]
               ]
              )
            ]
           ]
          ]))
