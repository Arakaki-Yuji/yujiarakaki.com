CreatedAt: 2020-11-09
Title: Clojureの開発環境をdocker-composeで作る

普段RailsやPHPなどでアプリケーション開発するときにはDocker/docker-composeをよく使うのでClojureでも使いたい！と思って環境をセットアップしました。

前提としてleiningenを使ったプロジェクトを対象にしています。

<a href="https://gyazo.com/552065f790ded9de06016c668c572546"><video alt="Video from Gyazo" width="1000" autoplay muted loop playsinline controls><source src="https://i.gyazo.com/552065f790ded9de06016c668c572546.mp4" type="video/mp4" /></video></a>


# Dockerfileを作る

clojure用の[公式イメージ](https://hub.docker.com/_/clojure/)が用意されているのでそれを使って以下のようなDockerfileを作ります。


```
FROM clojure:openjdk-8-lein
COPY project.clj /usr/src/app/
WORKDIR /usr/src/app
RUN lein deps
COPY . /usr/src/app

CMD ["lein", "repl", ":headless"]
```

ポイントは2行目で先にproject.cljをコピーし4行目でlein depsを実行して依存ライブラリのインストールを行っているところです。
これによりコンテナイメージの中で依存ライブラリが入っている状態が作れ、docker runでの起動処理がその分速くなります。先にlein depsしていないと、docker runでlein replを実行するたびに依存ライブラリのインストールが行われてしまいます。


# docker-compose.ymlを作る

以下のようなdocker-compose.ymlを作ります。

```
version: '3.4'

services:
  clojure:
    build: .
    environment:
      LEIN_REPL_PORT: 46123
      LEIN_REPL_HOST: 0.0.0.0
    ports:
      - '46123:46123'
    volumes:
      - .:/usr/src/app
```

ポイントは環境変数(environment)として`LEIN_REPL_PORT`と`LEIN_REPL_HOST`を渡しているところです。
`LEIN_REPL_PORT`は`lein repl`で起動したREPLサーバーがどのポートで接続を受け付けるかを指定します。
`LEIN_REPL_HOST`は`lein repl`で起動したREPLサーバーがどのホストからの接続を受け付けるかを指定します。

今回はPortは46123、Hostは0.0.0.0なのでどこからの接続でも受け付けるように指定しています。(Host: 0.0.0.0は開発環境だからゆるく設定していますが、本番・検証環境などではもっと厳しい設定にしてください。)

この状態で`docker-compose up --build`を実行するとlein replを起動したコンテナが立ち上がります。

# Emacsのciderからコンテナ内のREPLに接続する

ciderには `cider-connect`というコマンドがあってそのコマンドを実行すると接続したいREPLのHOSTとPORTを指定して接続することができます。

`M-x cider-connect`と打つと、cider-connectを使うことができます。
