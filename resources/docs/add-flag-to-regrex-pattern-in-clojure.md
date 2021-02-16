CreatedAt: 2021-02-17
Title: Clojureの正規表現パターンにフラグを追加する

Clojureで正規表現パターンを作るときは以下のように書く。

```:clojure
#"pattern"
=> #"pattern"

(re-find #"pattern" "this is a pattern") ;; 文字列の中からパターンが見つかるか探す
=> "pattern"

```

`#"pattern"` という記法で生成されるのは java.util.regex.Pattern クラスのインスタンスである。
java.util.regex.Patternはインスタンスを生成するcompileメソッドを呼び出すときにフラグを指定することで例えば大文字小文字を区別しないなどの挙動に変更することができる。
そのフラグ指定をClojureの `#"pattern"` 記法で指定したい場合、`#"(?i)pattern"`のように (?フラグ) を入れることで指定できる。


```:clojure
(class #"pattern")
=> java.util.regex.Pattern

(java.util.regex.Pattern/compile "pattern")
=> #"pattern"

(re-find (java.util.regex.Pattern/compile "pattern") 
         "this is a pattern")
=> "pattern"

(re-find (java.util.regex.Pattern/compile "pattern") 
         "this is a patteRN")
=> nil 

(re-find (java.util.regex.Pattern/compile "pattern" 
                                          java.util.regex.Pattern/CASE_INSENSITIVE) 
		"this is a patteRN")
=> "patteRN"

(re-find #"pattern" "this is a pattern")
=> "pattern"

(re-find #"pattern" "this is a patteRN")
=> nil

(re-find #"(?i)pattern" "this is a patteRN")
=> "patteRN"
```

# 参考
- [java.util.regex.Pattern](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#CASE_INSENSITIVE)
- [re-find](https://clojuredocs.org/clojure.core/re-find)
- [ignore-case-sensitive-in-clojure | Stack Overflow](https://stackoverflow.com/questions/53513843/ignore-case-sensitive-in-clojure)
- [Java interop](https://clojure.org/reference/java_interop)
