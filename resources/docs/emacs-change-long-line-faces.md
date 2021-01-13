CreatedAt: 2021-01-13
Title: Emacsでなぜか一行80文字以上入力すると文字の色が変わってしまう場合に文字数のしきい値を変更する

Emacsで色々設定をいれているうちに、いつの間にか一行80文字以上を入力すると以下のキャプチャ画像のようにその行の色が紫色っぽく変わってしまう人向けの記事です。

[![Image from Gyazo](https://i.gyazo.com/46ade536120ce9f994cfec07411ee3ba.png)](https://gyazo.com/46ade536120ce9f994cfec07411ee3ba)


これは、whitespace-modeというスペースやタブを色つけするモードを使うと起きる現象です。

whitespace-modeには、whitespace-line-columnという数値を入れる変数があり、その数値を超えると一行の文字数と多すぎるということで注意喚起するためにその行の色が変わるようになります。

whitespace-modeをoffにするとこの現象は消すことができるのですが、whitespace-modeをoffにしないで80文字変えても色を変えたくない、もしくはその文字数のしきい値を変更したい場合は whitespace-line-column に変更したい数値をsetqしてあげれば良いです。

```
(setq whitespace-line-column 300)
```

# どうやって上記の方法を見つけることができたか？
EmacsのFaceに関する公式ドキュメントを読んでいるときに list-faces-display というコマンドで 各faceの名前とそのFace(色、フォント、背景色など)が一覧で確認できることを知りました。

その一覧画面を見ながら文字数が80文字を変えたときに変わるに色が設定されているfaceを探し、そのfaceの詳細を見ることで whitespace-line-column という変数があることを知ることができました。

[![Image from Gyazo](https://i.gyazo.com/b8159dd7e475a3844bd0c7850d8668bc.gif)](https://gyazo.com/b8159dd7e475a3844bd0c7850d8668bc)

# 参考
- [WhiteSpace](https://www.emacswiki.org/emacs/WhiteSpace)
- [テキストのフェイス](https://ayatakesi.github.io/emacs/25.1/Faces.html#Faces)
