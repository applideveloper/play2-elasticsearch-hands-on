package controllers

import models.User
import play.api.mvc._

import play.api.libs.json._

import scala.concurrent.Future

object JsonController13 extends Controller with UserService {

  /**
   * 一覧表示
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


  /**
   * 一覧表示 (w/o implicit)
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
   * 一覧表示 (ベタ書き)
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

