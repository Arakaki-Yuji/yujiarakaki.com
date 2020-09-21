(ns docsite.templates.index
  (:require [hiccup.page :refer [html5]]
            [docsite.templates.partials.head :refer [head]]
            [docsite.templates.partials.header :refer [header]]))

(defn page []
  (html5 head
         [:body
          header
          [:main
           [:div.articleList
            [:div.article
             [:h3.articleTitle [:a {:href "/"} "記事のタイトル"] ]
             [:p.articlePublishedAt "公開日: 2020-09-23"]
             ]
            [:div.article
             [:h3.articleTitle [:a {:href "/"} "記事のタイトル"] ]
             [:p.articlePublishedAt "公開日: 2020-09-23"]
             ]
            [:div.article
             [:h3.articleTitle [:a {:href "/"} "記事のタイトル"] ]
             [:p.articlePublishedAt "公開日: 2020-09-23"]
             ]
            ]
           ]
          ]))
