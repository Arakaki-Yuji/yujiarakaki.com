(ns docsite.templates.article
  (:require [hiccup.page :refer [html5]]
            [docsite.templates.partials.head :refer [head]]
            [docsite.templates.partials.header :refer [header]]))

(defn page [content title]
  (html5 head
         [:body
          header
          [:main
           [:section.article
            [:h1.articleTitle title]
            [:div.content
             content
             ]
            ]
           ]
          ]))
