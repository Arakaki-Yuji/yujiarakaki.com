(ns docsite.templates.partials.footer)

(def footer
  [:footer
   [:div.footerWrapper
    [:h2 "プロフィール"]
    [:p "新垣雄志(Arakaki Yuji)。沖縄でベンチャー・スタートアップ界隈を主戦場としているソフトウェアエンジニア。現在はCBcloud株式会社に所属。本サイトではソフトウェアエンジニアとしての日々の活動や学習の記録、自分の考えを言語化して発信するための場として運営している。"]
    [:h3 "SNS"]
    [:ul
     [:li [:a {:href "https://twitter.com/arakaji"} "Twitter"]]
     [:li [:a {:href "https://github.com/Arakaki-Yuji"} "GitHub"]]
     [:li [:a {:href "https://www.facebook.com/yuuji.arakaki"} "Facebook"]]
     [:li [:a {:href "https://note.com/arakaji"} "note"]]
     ]
    ]
   ])
