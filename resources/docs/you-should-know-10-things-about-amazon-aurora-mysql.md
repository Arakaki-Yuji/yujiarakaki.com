CreatedAt: 2020-12-04
Title: Amazon Aurora MySQLを使うときに知っておきたい6つのこと

[CBcloud Advent Calendar 2020](https://qiita.com/advent-calendar/2020/cbcloud)の4日目を担当します、[あらかきゆうじ](https://twitter.com/arakaji)です。CBcloudではサーバーサイド & インフラエンジニアみたいな立ち位置で仕事をしております。

今回は「Amazon Aurora MySQLを使うときに知っておきたい10のこと」というタイトルの記事を書こうとしたのですが、書いてみると自分の頭の中にぼんやりとあった書きたいことが6つしかなかったことに気がついたので「Amazon Aurora MySQLを使うときに知っておきたい6つのこと」というタイトルに変更しました。

AWSのマネージドサービスである[Aurora](https://aws.amazon.com/jp/rds/aurora/?aurora-whats-new.sort-by=item.additionalFields.postDateTime&aurora-whats-new.sort-order=desc)を「マネージドの強いMySQL」ぐらいのお気持ちで使っているサーバーサイドエンジニアの方にAurora固有の特徴をある程度知ってもらえればいいな〜と思っています。

ちなみに弊社ではAuroraのMySQL互換を使っているので、今回はAurora自体の特徴とMySQL互換の特徴に絞って紹介していきます。

# 1. Auroraに接続するエンドポイントは4種類ある
Auroraは複数のインスタンスをクラスターにして稼働させることができるRDBのマネージドサービスですが、Auroraに接続するエンドポイントは4種類あります。

- Cluster endpoint
- Reader endpoint
- Custom endpoint
- instance endpoint

## Cluster endpoint
Cluster endpointはWriter endpointとも呼ばれますが、AuroraのClusterのうち Primary DB instanceに接続するエンドポイントです。Primary DB instanceに接続するため、このエンドポイントに接続すると書き込み/読み込み/DDLが可能になります。

Cluster endpointではFailoverの仕組みが備わっていて、もしPrimary DB instanceに障害が起きたときには別のinstanceがPrimary DB instanceとなり、同じエンドポイントのまま新しいPrimary DB instanceに接続するようになります。

## Reader endpoint
Reader endpointはその名前の通り読み込み専用のエンドポイントです。
Aurora Clusterは基本的に1つのPrimary instance(読み込み/書き込み/DDL)と複数のReplica instance(読み込み専用)を動かしますが、Reader endpointはその複数のReplica instanceに接続するためのエンドポイントになります。

ロードバランシング機能が備わっているため、Replica instanceを複数インスタンス動かしている場合はそれぞれのインスタンスにDB接続を負荷分散してくれます。

## Custom endpoint
Aurora Clusterのうちの任意のインスタンスをグループ化して、そのグループ化されたインスタンスに対して接続を行うためのエンドポイントです。こちらもReader endpointと同じくロードバランシング機能が備わっています。

たとえばアプリケーションが使っているAurora clusterのデータをBIツールでも参照したいがアプリケーションへの影響を最小化するために接続するDBインスタンスを分けたいみたいな用途に使うことができます。

デフォルトだとCustome endpointはつくられておらず、使いたい場合は自分で設定して作る必要があります。


## Instance endpoint
Aurora clusterの特定のDB instanceに接続するためのエンドポイントです。
もちろん特定のインスタンスに直接接続するため、そのインスタンスに障害が起きたときはロードバランシングもFailoverの仕組みもないので自分でコントロールする必要があります。

一般的には使いませんが、Auroraが提供するロードバランシングやFailoverの仕組みだと要件が足りずに自分でコントロールしたいという場合に使うことができます。


# 2. Cluster endpointのFailoverの仕組み
Cluster endpointはFailoverの仕組みが備わっています。
Cluster endpointで接続するPrimary db instanceに障害が発生すると2つの方法でFailoverを行います。

## Read Replicaがある場合
すでにRead Replicaが稼働している場合、そのRead Replicaのうち一つをPrimary db instanceとして昇格させて、Clsuter endpointの接続先を新しいPrimary db instanceに変更します。事前にどのReplicaをPrimary db に昇格させるかの優先順位を設定することができ、障害時にはその優先順位をもとにどのReplicaをPrimary db instanceに昇格させるか決定します。

この復旧にかかる時間が最低でも120秒以内、大抵の場合は60秒以内に復旧すると言われています。

## Read Replicaがない場合
稼働しているRead Replicaが一台もない場合、新しい Primary DB instanceを起動して、そこにCluster endpointの接続先を変更します。

この復旧にかかる時間の目安が10分以内程度だと言われています。

## Failoverの仕組みを使う注意点
上記のFailoverの仕組みは各エンドポイントのDNS名前解決の際に返されるIPアドレスを変更することで実現されています。もしAurora Clusterに接続するアプリケーションにDNS Cacheの仕組みが使われていた場合、そのキャッシュの有効期間はfailoverが正しく動かない可能性があります。

そのため接続するアプリケーション側にDNS Cacheの仕組みが使われていないか、使われている場合はそのキャッシュの有効期限(TTL)を短くする設定はないかを確認する必要があります。

# 3. 注意点は多いが、Multi-master clusterを作れる
通常のAurora Clusterはsingle master clusterといい、一つのPrimary db instance(読み込み/書き込み/DDL)と複数のRead replica instance(読み込みのみ)で構成されています。
Multi-master clusterの場合は複数のインスタンスでそれぞれ読み込み/書き込み/DDLができるようになります。
single-master clusterの場合は読み込みのクエリしか負荷分散できなかったものが書き込みのクエリも複数のインスタンスで負荷分散することができます。

ただし複数のインスタンスで同じデータを書き込み可能にするという特性上注意点も多いので、使う場合は慎重に検討する必要があります。

## 注意点

### ロードバランシング、Failoverの仕組みをアプリケーション側で実装する必要がある
複数のインスタンスにロードバランシングしてくれる仕組みがないので、アプリケーション側でInstance endpointへの接続を任意のロジックで切り替える必要があります。
アプリケーション側からinstance endpointで接続するため、cluster endpointに備わっているFailoverの仕組みが使えず、こちらも自前で実装する必要があります。

### Write conflictが起きないようなアプリケーション設計が必要
別のインスタンスで同一のデータを書き込みして変更しようとするとWrite conflictというエラーが発生します。アプリケーションのレイヤーでWrite conflictが発生する確率を下げるような制御が必要です。

たとえばマルチテナントアプリケーションの場合は、テナント毎に接続するDB インスタンスを決めるようにするとWrite conflictが発生する確率をへらす事ができます。

### Clusterのインスタンスは同じRegionにないといけない
single-master clusterの場合は、耐障害性を高めるために別のRegionにClusterのインスタンスを配置することができますが、Multi-master clusterの場合はすべてのインスタンスが同一リージョンにないといけません。

### 現時点(2020/12/04)では2インスタンスしか作れない
この記事を書いている時点はmulti-master cluster内には2インスタンスしか作ることができません。


# 4. Backtracking機能(clusterの状態を特定の時間の状態に戻す)が使える
Aurora MySQLだとBacktracking機能が搭載しており、clusterの状態を特定の時間の状態に戻すことができます。
たとえばDB操作しているときに誤ってテーブルをDROPしてしまった！というときにその操作をする前の状態に戻すことができます。

もちろんこれは無制限に戻す事ができるわけではなく、予め自分で設定しておいたbacktrack windowというBacktrackingが有効な時間の範囲内(最大72時間)で戻すことができます。

この機能は設定で有効にしないと使えないので、特に理由がなければ有効にしておいたほうがいいと思います。

# 5. メモリのサイズは、VolumeReadIOPSが低い状態で安定するまで大きくする
MySQLのパフォーマンスを最適化する上でメモリのサイズは重要です。
自分のアプリケーション上で行われるDBの操作でよく使われるデータがMySQLのメモリ上に乗っており、ディスクへのアクセスが少ない状態が維持できるとパフォーマンスが安定して高くなります。ただ必要以上にメモリを大きくするとパフォーマンスがそれ以上高くならないのに余計な費用がかかるので最適なサイズを選ぶことが必要です。

その最適化のための指標としてVolumeReadIOPSが低い状態で安定するまでメモリサイズを上げるという方法があります。

VolumeReadIOPSとはAurora clusterのVolumeで一定時間内で行われたREAD I/O操作回数の平均値です。この指標が低い状態で安定している場合はよく使うデータがメモリ上に乗っており、Volumeへのアクセスが少ない状態だと言えます。逆にこれが不安定だとメモリに乗り切れておらず何度もボリュームへのアクセスが発生しておりその分パフォーマンスへの悪影響が出ていると言えます。VolumeReadIOPSが安定して低い状態で、さらにメモリサイズを上げてもすでに十分な量のメモリが備わっているのでそれ以上メモリ起因でのパフォーマンス向上が起きるということはありません。(メモリを増やすことで接続可能上限数が増えるという別の側面がありますが、それはまた別の話)

# 6. Aurora MySQL5.6互換だと、通常のMySQL5.6のInnoDBでは使えない空間インデックスが使える
MySQL 5.6のInnoDBの場合空間インデックスを作ることはできず、MySQL 5.7からしか作ることができないのですが、AuroraのMySQL5.6互換のエンジンだと空間インデックスを作る事ができます。

もしローカルでは通常のMySQL5.6、本番のみAuroraを使っているような場合は本番だけにインデックスを貼るという作業をする必要があり注意が必要ですが、これにより緯度経度の範囲検索などがインデックスなしよりもかなり高速化されるのでもし知らなくて使っていない人がいればぜひ使ってみてほしいです。


# 参考URL
- [Amazon Aurora connection management](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/Aurora.Overview.Endpoints.html)
- [High availability for Amazon Aurora](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/Concepts.AuroraHighAvailability.html)
- [Overview of Amazon Aurora MySQL](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/Aurora.AuroraMySQL.Overview.html)
- [Single-master replication with Amazon Aurora MySQL](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/AuroraMySQL.Replication.html)
- [Backtracking an Aurora DB cluster](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/AuroraMySQL.Managing.Backtrack.html)
- [Working with Aurora multi-master clusters](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/aurora-multi-master.html)
- [Best practices with Amazon Aurora](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/Aurora.BestPractices.html)


# いつものやつ
CBcloudではソフトウェアエンジニアを募集しています。僕は沖縄拠点で働いているので沖縄で一緒に働いてくれる人は個人的にも強く募集しています！

Rails, AWS, Nuxt.js, Flutter辺りに興味のある方は無限にチャレンジできる環境があるので、きっとソフトウェアエンジニアとして成長できる良い機会と思います。

もし少しでも興味のある方はTwitterへの僕にDMしてもよいですし、以下の求人ページからお問い合わせしてみてください。

- [沖縄オフィス立ち上げ社員募集](https://www.wantedly.com/projects/432129)
