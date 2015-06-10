# 各種依存ファイルのインストール

Java には依存関係を解決するために`Maven`等が利用されますが、Scalaでは一般的に`sbt`が利用されます。Play2では`sbt`に少し機能を付与した`activator`を利用します。
ここではオリジナルのハンズオンと少し趣向を変え、基本的なクラスだけ既に含まれたスケルトンクラスから始めることにしましょう。


## スケルトンプロジェクトでコンパイル

activatorをローカルにインストールすることは不要です。

コマンドプロンプトで以下のコマンドを実行します。

    $ cd play2-hands-on
    $ ./activator
    [play2-hands-on] $ compile

初回は、activatorの起動とcompileを合わせて30分程度かかることがあります。

ブラウザから http://localhost:9000/ にアクセスし、以下の画面が表示されることを確認します。

[[images/welcome.png]]

> **POINT**
> * `activator run`で実行している間はホットデプロイが有効になっているため、ソースを修正するとすぐに変更が反映されます
> * CTRL+Dで`activator run`での実行を終了することができます
> * `activator  run`で実行中に何度も修正を行っているとヒープが不足してプロセスが終了してしまったりエラーが出たまま応答がなくなってしまう場合があります
> * プロセスが終了してしまった場合は再度`activator run`を実行してください
> * 応答しなくなってしまった場合は一度コマンドプロンプトを閉じ、再度起動して`activator run`を実行してください

## 依存関係を確認

Elasticsearchのを依存関係を確認するために、`build.sbt`開きます。

```scala
name := "play2-hands-on"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  //  jdbc,
  //  anorm,
  //  cache,
  //  ws,
  // Elasticsearch and JSON
  //"com.typesafe.play"           %%  "play-slick"            % "0.8.1",
  "com.sksamuel.elastic4s"      %%  "elastic4s"             % "1.5.6"
    excludeAll(
    ExclusionRule(organization = "org.scala-lang", name = "scala-library"),
    //      ExclusionRule(organization = "org.apache.lucene", name = "lucene-analyzers-common"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-highlighter"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-grouping"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-join"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-memory"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-misc"),
    //      ExclusionRule(organization = "org.apache.lucene", name = "lucene-queries"),
    //      ExclusionRule(organization = "org.apache.lucene", name = "lucene-queryparser"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-sandbox"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-spatial"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-suggest")
    )
)
```
