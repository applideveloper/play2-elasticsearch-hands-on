## ツールプロジェクトの準備

[slick-codegen.zip](https://github.com/bizreach/play2-hands-on/releases/download/20150329/slick-codegen.zip) をダウンロードし、以下のように`play2-hands-on`プロジェクトと同じディレクトリに展開します。

```
+-/play2-hands-on
|   |
|   +-/app
|   |
|   +-/conf
|   |
|   +-...
|
+-/slick-codegen
    |
    +-/project
    |
    +-/src
    |
    +-...
```

## H2の起動

まず、`slick-codegen`プロジェクトの`h2/start.bat`をダブルクリックしてH2データベースを起動します。データベースには以下のスキーマのテーブルが作成済みの状態になっています。

[[images/er_diagram.png]]

## モデルの自動生成

SlickではタイプセーフなAPIを使用するために

* タイプセーフなクエリで使うテーブル定義
* エンティティオブジェクト

を用意する必要がありますが、これらはSlickが標準で提供しているジェネレータを使用することでDBスキーマから自動生成することができます。

`slick-codegen`プロジェクトのルートディレクトリで以下のコマンドを実行します。

```
sbt gen-tables
```

すると`play2-hands-on`プロジェクトの`app/models`パッケージにモデルクラスが生成されます。

[[images/gen_model.png]]

## DB接続の設定

`play2-hands-on`プロジェクトの`conf/application.conf`にDB接続のための設定を行います。

**変更前：**

```properties
# db.default.driver=org.h2.Driver
# db.default.url="jdbc:h2:mem:play"
# db.default.user=sa
# db.default.password=""
```

**変更後：**

```properties
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:tcp://localhost/data"
db.default.user=sa
db.default.password=sa
```

----
[[＜IDEの準備に戻る|02.IDEの準備]] | [[ルーティングの定義に進む＞|04.ルーティングの定義]]