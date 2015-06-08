package controllers

import java.util

import com.sksamuel.elastic4s._
import com.sksamuel.elastic4s.ElasticDsl._
import models.{User, QueryCondition}
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.{SearchHit, SearchHits}
import org.elasticsearch.transport.RemoteTransportException
import play.api.mvc._

import play.api.db.slick._
import models.Tables._
import profile.simple._

import play.api.libs.json._
import utils.ElasticsearchUtil

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object JsonController extends Controller {

  // UsersRowをJSONに変換するためのWritesを定義
  implicit val usersRowWritesFormat = Json.writes[UsersRow]

  // ユーザ情報を受け取るためのケースクラス
  case class UserForm(name: String, companyId: Option[String])

  // JSONをUserFormに変換するためのReadsを定義
  implicit val userFormFormat = Json.reads[UserForm]


  /**
   * 一覧表示
   */
  def list = Action.async { implicit rs =>
    val future = ElasticsearchUtil.process { client =>

      val query = search in "handson" -> "users" sort(by field "userId")

      client.execute(query).map { res =>
        res.getHits.getHits.map { (hit: SearchHit) =>
          User(
            userId = hit.getSource.get("userId").asInstanceOf[String],
            name = hit.getSource.get("name").asInstanceOf[String],
            companyId = Option(hit.getSource.get("companyId")).asInstanceOf[Option[String]]
          )
        }
      }
    }

    implicit val usersWrites = Json.writes[User]

    future.map{ users => Ok(Json.obj("users" -> users))}
  }






  /**
   * 一覧表示 (コメントつき)
   */
  def listWithComment = Action.async { implicit rs =>
    val future = ElasticsearchUtil.process { client =>

      // IDの昇順にすべてのユーザ情報を取得
      // elastic4sのクエリDSL
      // https://github.com/sksamuel/elastic4s/blob/master/guide/search.md
      val query = search in "handson" -> "users" sort(by field "userId")

      // elastic4sのクエリ実行。結果はFutureで返ってくる
      val response: Future[SearchResponse] = client.execute(query)

      // 正常時の後続処理をmapで書く
      val userList: Future[Array[User]] = response.map { res =>
        // 正常時のヒット
        val searchHits: SearchHits = res.getHits

        // JSONのhitsに相当する部分
        val hits: Array[SearchHit] = searchHits.getHits

        // 配列を
        hits.map { (hit: SearchHit) =>

          // JSONのsourceに相当する部分
          val source: java.util.Map[String, Object] = hit.getSource

          // case class で返す
          User(
            userId = source.get("userId").asInstanceOf[String],
            name = hit.getSource.get("name").asInstanceOf[String],
            companyId = Option(hit.getSource.get("companyId")).asInstanceOf[Option[String]]
          )
        }
      }
      
      // Future の結果として返るもの
      userList
    }

    // case class からJSONへの変換のためのPlay!上のおまじない
    implicit val usersWrites = Json.writes[User]

    // ユーザの一覧をJSONで返す
    // Future の結果は Play! が処理してくれる
    future.map{ users => Ok(Json.obj("users" -> users))}
  }







  /**
   * ユーザ登録
   */
  def createUser() = Action.async(parse.json) { implicit rs =>
    rs.body.validate[UserForm].map { form =>
      // OKの場合はユーザを登録

      // case class のインスタンス作成
      val user = User.create(form.name, form.companyId)

      val future = ElasticsearchUtil.process { client =>
        val command = index into "handson" -> "users" fields(
          "userId" -> user.userId,
          "name" -> user.name,
          "companyId" -> user.companyId
          )

        client.execute(command)
      }

      // Future の結果をハンドリング
      future.map { res =>
        Ok(Json.obj("result" -> "success"))
      }.recover {
        // サーバーエラー
        case ex: RemoteTransportException =>
          BadRequest(Json.obj("result" ->"failure", "error" -> ex.getMessage))
        // 予期せぬエラー
        case ex =>
          InternalServerError

      }

    }.recoverTotal { e =>
      // NGの場合はバリデーションエラーを返す
      Future(BadRequest(Json.obj("result" ->"failure", "error" -> JsError.toFlatJson(e))))
    }
  }






  /**
   * ユーザ更新
   */
  def updateUser(userId: String) = Action.async(parse.json) { implicit rs =>
    rs.body.validate[UserForm].map { form =>
      // OKの場合はユーザを登録

      // case class のインスタンス作成
      val user = User(userId, form.name, form.companyId)

      val future = ElasticsearchUtil.process { client =>
        val command = update id user.userId in "handson" -> "users" doc(
          "userId" -> user.userId,
          "name" -> user.name,
          "companyId" -> user.companyId
          )

        client.execute(command)
      }

      // Future の結果をハンドリング
      future.map { res =>
        Ok(Json.obj("result" -> "success"))
      }.recover {
        // サーバーエラー
        case ex: RemoteTransportException =>
          BadRequest(Json.obj("result" ->"failure", "error" -> ex.getMessage))
        // 予期せぬエラー
        case ex =>
          InternalServerError

      }

    }.recoverTotal { e =>
      // NGの場合はバリデーションエラーを返す
      Future(BadRequest(Json.obj("result" ->"failure", "error" -> JsError.toFlatJson(e))))
    }
  }






  /**
   * ユーザ削除
   */
  def removeUser(userId: String) = Action.async { implicit rs =>

    val future = ElasticsearchUtil.process { client =>
      val command = delete id userId from "handson" -> "users"

      client.execute(command)
    }

    // Future の結果をハンドリング
    future.map { res =>
      Ok(Json.obj("result" -> "success"))
    }.recover {
      // サーバーエラー
      case ex: RemoteTransportException =>
        BadRequest(Json.obj("result" ->"failure", "error" -> ex.getMessage))
      // 予期せぬエラー
      case ex =>
        InternalServerError

    }
  }






  /**
   * 一覧表示(おまけ)
   */
  def listEntireSource = Action.async { implicit rs =>

    // たとえば、QueryStringでパラメータを渡す
    val cond = QueryCondition.form.bindFromRequest().value.getOrElse(QueryCondition())

    val future = ElasticsearchUtil.process { client =>
      val definition = search in "handson" -> "users" size cond.size from cond.from

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
