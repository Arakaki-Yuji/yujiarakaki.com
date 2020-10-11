(ns docsite.templates.partials.header)

(def header
  [:header
   [:div.headerWrapper
    [:a {:href "/profile.html"}
     [:img.brandLogo {:src "/assets/images/icon.jpg"}]
     ]
    [:div.brandTitle
     [:a {:href "/"} "Yuji Arakaki"]]
    ]
   ])
