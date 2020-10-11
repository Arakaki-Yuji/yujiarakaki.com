(ns docsite.templates.partials.header)

(def header
  [:header
   [:div.headerWrapper
    [:img.brandLogo {:src "/assets/images/icon.jpg"}]
    [:div.brandTitle
     [:a {:href "/"} "Yuji Arakaki"]]
    ]
   ])
