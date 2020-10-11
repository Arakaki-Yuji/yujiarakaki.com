(ns docsite.templates.article
  (:require [hiccup.page :refer [html5]]
            [docsite.config :refer [site-title]]
            [docsite.templates.partials.head :refer [head]]
            [docsite.templates.partials.header :refer [header]]
            [docsite.templates.partials.footer :refer [footer]]
            ))

(defn page [content title]
  (html5 (head (str title " | " site-title))
         [:body.articleDetailPage
          header
          [:main
           [:section.article
            [:h1.articleTitle title]
            [:div.content
             content
             ]
            ]
           ]
          footer
          ]))
