# Play2 + Elasticsearch ハンズオン

## インデックス

* [01.プロジェクトの作成](https://github.com/bizreach/play2-hands-on/wiki/01.%E3%83%97%E3%83%AD%E3%82%B8%E3%82%A7%E3%82%AF%E3%83%88%E3%81%AE%E4%BD%9C%E6%88%90)
* [02.IDEの準備](https://github.com/bizreach/play2-hands-on/wiki/02.IDE%E3%81%AE%E6%BA%96%E5%82%99)
* [03.Elasticsearchの準備](doc/03-prepare-elasticsearch.md)
* [11.コントローラの雛形の作成](doc/11-controller-skeleton.md)
* [12.ルーティングの定義](doc/12-define-routes.md)
* [13.ユーザ一覧APIの仮実装](doc/13-implement-temp-users.md)
* [21.Elasticsearchの概要](doc/21-elasticsearch-overview.md)
* [22.ElasticsearchのCRUD操作](doc/22-elasticsearch-crud.md)
* [31.ユーザ一覧APIの本実装](doc/31-list.md)
* [32.ユーザ登録・更新・削除APIの実装](doc/32-update.md)

## 目的

オリジナルの[play2-hands-on](https://github.com/bizreach/play2-hands-on/)から派生して、合わせてElasticsearchを使ってWebアプリケーションを作成するハンズオンです。

元々の目的は次のようなものでした。

* Scalaに触れてもらう
* 数時間でとりあえず動くものを作ってみる

一方、当ハンズオンでは以下の差分があります。

* (+)Elasticsearchに触れてもらう
* (+)[elastic4s](https://github.com/sksamuel/elastic4s)でElasticsearchを操作する
* (-)Slick(RDB操作)には触れない
* (-)Twirl(UI描画)には触れない

と同様のモデルを用いており、オリジナルと比較することでよりいっそう理解が深まるようにと意図しています。


## 構成

使用するフレームワークおよびバージョンは以下の通りです。

* Play 2.3.x (2015/6/8現在、2.4.0がリリースされています)
* Elasticsearch 1.5.x

随時、最新の内容に更新していきます。


## 前提条件

このハンズオンを実施するにあたっての前提条件は以下の通りです。

* JavaおよびWebアプリケーションの開発に関する基本的な知識を持っていること
* JDK 1.7以降がインストールされていること
* EclipseもしくはIntelliJ IDEAの最新版がインストールされていること


## 内容

ユーザ情報のCRUDを行う簡単なアプリケーションを作成します。

* ユーザ一覧のJSONを返す
* 新規ユーザ登録をおこなう
* ユーザ情報を編集する
* ユーザを削除する

![作成するアプリケーションの画面遷移](https://github.com/bizreach/play2-hands-on/wiki/images/flow.png)

また、後半ではこのアプリケーションと同じCRUD処理を行うJSONベースのWeb APIも作成します。
