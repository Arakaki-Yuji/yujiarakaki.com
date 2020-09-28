(ns docsite.import
  (:require [clojure.string :as string]))

(defn- split-by-page [import-text]
  (string/split import-text
                #"--------"))

(defn- split-to-meta-and-body [page-text]
  (string/split page-text
                #"-----\n"))

(defn- split-by-line [text]
  (string/split text
                #"\n"))

(defn- filter-empty-string [string-collection]
  (filter #(> (count (string/trim %))
              0)
          string-collection))

(defn- parse-meta [meta-text]
  (let [attribute-collection (for [meta-text-a-line (filter-empty-string (split-by-line meta-text))]
                               (let [attribute (string/split meta-text-a-line
                                                             #":")]
                                 {(attribute 0) (string/trim (attribute 1))})
                               )]
    (reduce into attribute-collection)))

(defn- parse-page [page-text]
  (if (> (count page-text) 0)
    (let [[meta body] (split-to-meta-and-body page-text)]
      {:meta (parse-meta meta)
       :body (last (string/split body #"^BODY:"))})
    nil))

(defn parse [import-file-path]
  ;; はてなブログのexportファイルをparseして後処理可能なデータフォーマットに変換する
  (let [text (slurp import-file-path)
        pages (filter-empty-string (split-by-page text))]
    (for [p pages]
      (parse-page p))
    )
  )

(defn page-name [page-data]
  (string/replace (get-in page-data [:meta "BASENAME"])
                  #"\/"
                  "-"))
