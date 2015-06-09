# コントローラの雛形を作る

当ハンズオンは、REST API を想定した実装をします。そのためビューを用意しませんが、同様にコントローラは用意します。


## コントローラの雛形を作る

`controllers`パッケージに`JsonController`オブジェクトを以下のように作成します。

```scala
package controllers

import play.api.mvc._
import play.api.libs.json._

object JsonController extends Controller {

  /**
   * 一覧
   */
  def listUsers = TODO

  /**
   * 登録
   */
  def createUser = TODO

  /**
   * 更新
   */
  def updateUser(id: String) = TODO

  /**
   * 削除
   */
  def removeUser(id: String) = TODO

}
```

> **POINT**
> * `object`はシングルトンなオブジェクトを定義するときに使います
> * メソッドは`def`キーワードで定義します
> * `TODO`メソッドは`Action not implemented yet.`という`501 NOT_IMPLEMENTED`レスポンスを返します
> * `play.api.libs.json._`はPlay2のJSONサポート機能を使用するために必要なimport文です
> * この後の実装のDSLと混同する可能性があるため、オリジナルハンズオンとは少し異なるメソッド名をつけています。
> * この後にブラウザ上でJSONを操作することがありますが、意図せぬ桁落ちを防ぐために`id`は`Long`ではなく`String`にしています。
