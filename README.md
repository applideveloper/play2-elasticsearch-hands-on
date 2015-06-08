# Play2 + Elasticsearch ハンズオン

> 2015/6/8 現在、鋭意整備中です。。。特にWikiが全く更新出来ていません。

## インデックス

* [01.プロジェクトの作成](https://github.com/bizreach/play2-hands-on/wiki/01.%E3%83%97%E3%83%AD%E3%82%B8%E3%82%A7%E3%82%AF%E3%83%88%E3%81%AE%E4%BD%9C%E6%88%90)
* [02.IDEの準備](https://github.com/bizreach/play2-hands-on/wiki/02.IDE%E3%81%AE%E6%BA%96%E5%82%99)
* [03.Elasticsearchの準備](doc/03-prepare-elasticsearch.md)
* [04.ルーティングの定義](https://github.com/bizreach/play2-hands-on/wiki/04.%E3%83%AB%E3%83%BC%E3%83%86%E3%82%A3%E3%83%B3%E3%82%B0%E3%81%AE%E5%AE%9A%E7%BE%A9)
* [05.ユーザ一覧の実装](https://github.com/bizreach/play2-hands-on/wiki/05.%E3%83%A6%E3%83%BC%E3%82%B6%E4%B8%80%E8%A6%A7%E3%81%AE%E5%AE%9F%E8%A3%85)
* [06.ユーザ登録・編集画面の実装](https://github.com/bizreach/play2-hands-on/wiki/06.%E3%83%A6%E3%83%BC%E3%82%B6%E7%99%BB%E9%8C%B2%E3%83%BB%E7%B7%A8%E9%9B%86%E7%94%BB%E9%9D%A2%E3%81%AE%E5%AE%9F%E8%A3%85)
* [07.登録、更新処理の実装](https://github.com/bizreach/play2-hands-on/wiki/07.%E7%99%BB%E9%8C%B2%E3%80%81%E6%9B%B4%E6%96%B0%E5%87%A6%E7%90%86%E3%81%AE%E5%AE%9F%E8%A3%85)
* [08.削除処理の実装](https://github.com/bizreach/play2-hands-on/wiki/08.%E5%89%8A%E9%99%A4%E5%87%A6%E7%90%86%E3%81%AE%E5%AE%9F%E8%A3%85)
* [09.JSON APIの準備](https://github.com/bizreach/play2-hands-on/wiki/09.JSON-API%E3%81%AE%E6%BA%96%E5%82%99)
* [10.ユーザ一覧APIの実装](https://github.com/bizreach/play2-hands-on/wiki/10.%E3%83%A6%E3%83%BC%E3%82%B6%E4%B8%80%E8%A6%A7API%E3%81%AE%E5%AE%9F%E8%A3%85)
* [11.ユーザ登録・更新APIの実装](https://github.com/bizreach/play2-hands-on/wiki/11.%E3%83%A6%E3%83%BC%E3%82%B6%E7%99%BB%E9%8C%B2%E3%83%BB%E6%9B%B4%E6%96%B0API%E3%81%AE%E5%AE%9F%E8%A3%85)
* [12.ユーザ削除APIの実装](https://github.com/bizreach/play2-hands-on/wiki/12.%E3%83%A6%E3%83%BC%E3%82%B6%E5%89%8A%E9%99%A4API%E3%81%AE%E5%AE%9F%E8%A3%85)
* [13.Tips](https://github.com/bizreach/play2-hands-on/wiki/13.Tips)

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
