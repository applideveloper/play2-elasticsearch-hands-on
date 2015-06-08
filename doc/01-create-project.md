## Play本体のインストール

http://www.playframework.com/download からtypesafe-activator-1.3.2-minimal.zipをダウンロードし、解凍したディレクトリを環境変数PATHに追加します。

## 新規プロジェクト作成

コマンドプロンプトで以下のコマンドを実行します。途中でScalaアプリケーションとJavaアプリケーションのどちらを作成するかを聞かれるのでScalaアプリケーションを選択します。

```
activator new play2-hands-on
```

[[images/create_project.png]]

作成した`play2-hands-on`ディレクトリに移動し、以下のコマンドでプロジェクトを実行します。

```
activator run
```

ブラウザから http://localhost:9000/ にアクセスし、以下の画面が表示されることを確認します。

[[images/welcome.png]]

> **POINT**
> * `activator run`で実行している間はホットデプロイが有効になっているため、ソースを修正するとすぐに変更が反映されます
> * CTRL+Dで`activator run`での実行を終了することができます
> * `activator  run`で実行中に何度も修正を行っているとヒープが不足してプロセスが終了してしまったりエラーが出たまま応答がなくなってしまう場合があります
> * プロセスが終了してしまった場合は再度`activator run`を実行してください
> * 応答しなくなってしまった場合は一度コマンドプロンプトを閉じ、再度起動して`activator run`を実行してください

## Slickを依存関係に追加

Slickを依存関係に追加するために、`build.sbt`を以下のように修正します。

```scala
name := "play2-hands-on"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
//  jdbc,
//  anorm,
//  cache,
//  ws,
  "com.typesafe.play" %% "play-slick" % "0.8.1"
)
```

----
[[＜ホームに戻る|Home]] | [[IDEの準備へ進む＞|02.IDEの準備]]
