# 13.ユーザ一覧APIの仮実装

Elasticsearch へアクセスせずに、固定データを返すユーザー一覧を実装します。

```scala
package controllers

import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.Future


// POINT 1
case class User(userId: String, name: String, companyId: Option[String] = None)

// POINT 2
object JsonController extends Controller with UserService {

  /**
   * 一覧
   */
   def listUsers = Action.async { implicit rs =>
     val users = fetchUsers

     implicit val usersWrites = Json.writes[User]

     Future.successful(
       Ok(
         Json.obj("users" -> users)
       )
     )
   }

  // 以下略
}


// POINT 3
trait UserService {

  def fetchUsers: List[User] = {
    List(
      User(
        "temp-u1", "Soichiro Minami", Some("BizReach")
      ),
      User(
        userId = "temp-u2",
        name = "Makoto Nagata"
      ),
      User(
        "temp-u3", "Shin Takeuchi"
      )
    )
  }
}

```

> **POINT 1**
> * Scala には`case class`という特別なクラスがあり頻繁に利用します
> * インタンス変数とコンストラクタを同時に定義するような形式で、数行で書けます
> * `case class`の変数は基本的には`イミュータブル`です。userId, name, companyId は変更できません
> * デフォルト値の設定が可能です。ここでは companyId = None の部分

> **POINT 2**
> * データを`case class`のList形式で取得し、JSONに変換して返却します
> * `Json.writes`はJSONに書き出すためのおまじないで、実際にはマクロで処理の中身が生成されています
> * `implicit` キーワードを宣言することで、暗黙的に変数を渡すことができます
> * この後に備え、Futureで包んで結果を返しています。PlayがFutureの中身を適切に取り出してくれます

> **POINT 3**
> * trait を用いて、処理をミックスインしています。DBアクセスなどがtraitとして実装されたりします
> * リストなどの作成は、たとえば、List()中にカンマ区切りで列記するだけです
> * `case class`のインスタンス生成は、値を列挙するだけも可能ですが、紛らわしい場合などには名前付き引数とします
> * メソッドの戻り型は大抵の場合は不要ですが、パブリックなメソッドでは明記することが多いです


## もう少し違った書き方で・・・

```scala
package controllers

import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.Future

object JsonController extends Controller with UserService {

  /**
    * 一覧表示その2 (w/o implicit)
    */
   def listUsers2 = Action.async { implicit rs =>
     val users = fetchUsers

     val usersWrites: Writes[User] = new Writes[User] {
       override def writes(user: User): JsValue =
         JsObject(List(
           "userId" -> JsString(user.userId),
           "name" -> JsString(user.name),
           "companyId" -> JsString(user.companyId.orNull)
         ))
     }

     Future.successful(
       Ok(
         Json.obj("users" -> Writes.traversableWrites(usersWrites).writes(users))
       )
     )
   }


   /**
    * 一覧表示その3 (ベタ書き)
    */
   def listUsers3 = Action.async { implicit rs =>
     Future.successful(
       Ok(
         Json.obj("users" ->
           JsArray(List(
             JsObject(List(
               "userId" -> JsString("temp-u1"),
               "name" -> JsString("Soichiro Minami"),
               "companyId" -> JsString("BizReach")
             )),
             JsObject(List(
               ("userId", JsString("temp-u2")),
               ("name", JsString("Makoto Nagata"))
             )),
             JsObject(List(
               Tuple2("userId", JsString("temp-u3")),
               Tuple2("name", JsString("Shin Takeuchi"))
             ))
           ))
         )
       )
     )
   }
}

```

> **POINT**
> * `implicit` を使わない書き方
> * JSONオブジェクトをベタ書きする書き方
