(ns docsite.templates.partials.head
  (:require [docsite.config :refer [site-title]]))

(defn head [title]
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   [:title title]
   [:link {:rel "stylesheet" :href "/assets/css/main.css"}]

   [:link {:ref "apple-touch-icon" :sizes "180x180" :href "/apple-touch-icon.png"}]
   [:link {:ref "icon" :type "image/png" :sizes "32x32" :href "/favicon-32x32.png"}]
   [:link {:ref "icon" :type "image/png" :sizes "16x16" :href "/favicon-16x16.png"}]
   [:link {:ref "manifest" :href "/site.webmanifest"}]
   [:link {:ref "manifest" :href "/safari-pinned-tab.svg" :color "#5bbad5"}]
   [:meta {:name "msapplication-TileColor" :content "#da532c"}]
   [:meta {:name "theme-color" :content "#ffffff"}]

   ;; ogp tags
   [:meta {:property "og:title" :content title}]
   [:meta {:property "og:type" :content "website"}]
   ;; [:meta {:property "og:description" :content ""}]
   ;; [:meta {:property "og:url" :content ""}]
   [:meta {:property "og:site_name" :content site-title}]
   [:meta {:property "og:image" :content "https://yujiarakaki.com/assets/images/icon.jpg"}]

   ;; twitter card
   [:meta {:name "twitter:card" :content "summary"}]
   [:meta {:name "twitter:site" :content "@arakaji"}]
   ])
