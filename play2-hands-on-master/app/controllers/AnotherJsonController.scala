package controllers

import com.sksamuel.elastic4s.ElasticDsl._
import models.{Company, QueryCondition, User}
import play.api.mvc._
import play.api.libs.json._
import utils.ElasticsearchUtil

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object AnotherJsonController extends Controller with UserService {

  /**
   * 一覧表示
   */
  def listUsers2 = Action.async { implicit rs =>
    val users = fetchUsers

    implicit val companyWrites = Json.writes[Company]
    implicit val userWrites = Json.writes[User]

    Future.successful(
      Ok(
        Json.obj("users" -> users)
      )
    )
  }


  /**
   * 一覧表示 (w/o implicit)
   */
  def listUsers3 = Action.async { implicit rs =>
    val users = fetchUsers

    val usersWrites: Writes[User] = new Writes[User] {
      override def writes(user: User): JsValue = {
        val js = JsObject(List(
          "userId" -> JsString(user.userId),
          "name" -> JsString(user.name)
        ))

        user.company match {
          case Some(c) =>
            js ++ JsObject(List("company" ->
              JsObject(List(
                "companyId" -> JsString(c.companyId),
                "name" -> JsString(c.name)
              ))
            ))
          case None =>
            js
        }
      }
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
  def listUsers4 = Action.async { implicit rs =>
    Future.successful(
      Ok(
        Json.obj("users" ->
          JsArray(List(
            JsObject(List(
              "userId" -> JsString("temp-u1"),
              "name" -> JsString("Soichiro Minami"),
              "company" -> JsObject(List(
                "companyId" -> JsString("c11"),
                "name" -> JsString("BizReach")
              ))
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






  /**
   * 一覧表示(おまけ)
   */
  def listEntireSource = Action.async { implicit rs =>

    // たとえば、QueryStringでパラメータを渡す
    val cond = QueryCondition.form.bindFromRequest().value.getOrElse(QueryCondition())

    val future = ElasticsearchUtil.process { client =>
      val definition = search in "handson" -> "users" size cond.size from cond.from sort(by field "userId")

      client.execute(definition).map { res =>
        res.getHits.getHits.map { hit =>
          // sourceを全部返す
          hit.getSource
        }
      }
    }

    // java.util.Map[String, Object] からJSONへの変換のためのPlay!上のおまじない
    import ElasticsearchSourceWrites.objectWrites

    future.map{ users => Ok(Json.obj("users" -> users))}
  }
}


trait UserService {

  def fetchUsers: List[User] = {
    List(
      User(
        "temp-u1", "Soichiro Minami", Some(Company("c11","BizReach"))
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

/**
 * source全表示用(おまけ)
 */
object ElasticsearchSourceWrites {

  import scala.collection.JavaConverters._

  implicit val objectWrites: Writes[Object] = new Writes[java.lang.Object] {

    override def writes(o: Object): JsValue = o match {
      case value: JsObject => value
      case value => value match {
        case v: String => JsString(v)
        case v: java.lang.Boolean => JsBoolean(v)
        case v: java.lang.Integer => JsNumber(BigDecimal(v))
        case v: java.lang.Long => JsNumber(BigDecimal(v))
        case v: java.util.ArrayList[_] => Writes.arrayWrites[AnyRef].writes(v.asInstanceOf[java.util.ArrayList[AnyRef]].asScala.toArray)
        case v: java.util.HashMap[_, _] => Writes.mapWrites[AnyRef].writes(v.asInstanceOf[java.util.HashMap[String, AnyRef]].asScala.toMap)
        case v: Array[_] => Writes.arrayWrites[AnyRef].writes(v.asInstanceOf[Array[AnyRef]])
        case v: Map[_, _] => Writes.mapWrites[AnyRef].writes(v.asInstanceOf[Map[String, AnyRef]])
        case v => JsString(v.toString) // TODO add pattern more later
      }
    }
  }
}

