# ルーティングの設定

クライアントから送信されたリクエストは、`conf/routes`の設定に従ってコントローラのメソッドへルーティングされます。
以下の設定を追記します。

```bash
GET       /users                  controllers.JsonController.listUsers

POST      /users                  controllers.JsonController.createUser

PUT       /users/:id              controllers.JsonController.updateUser(id: String)

DELETE    /users/:id              controllers.JsonController.removeUser(id: String)
```

> **POINT**
> * 今回は、RESTfulな設計とし、4つのHTTPメソッドを利用します
> * パラメータ部分は複数指定でき、正規表現も利用できます。
> * 上から順に評価されます。
> * マッピング定義で引数の型を省略すると、`String`になります
> * routesのコメントに日本語を記述するとコンパイルエラーになることがあります
> * 実際には、Scalaファイルが生成されます。生成先: `target/scala-2.11/src_managed/main/routes_routing.scala`
