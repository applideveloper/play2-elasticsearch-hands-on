package controllers

import com.sksamuel.elastic4s.ElasticDsl._
import models.{Company, User}
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.{SearchHit, SearchHits}
import org.elasticsearch.transport.RemoteTransportException
import play.api.mvc._

import play.api.libs.json._
import utils.ElasticsearchUtil

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object JsonController extends Controller {


  /**
   * 一覧表示
   */
  def listUsers = Action.async { implicit rs =>
    val future = ElasticsearchUtil.process { client =>

      val query = search in "handson" -> "users" sort(by field "userId")

      client.execute(query).map { res =>
        res.getHits.getHits.map { (hit: SearchHit) =>
          val user = User(
            userId = hit.getSource.get("userId").asInstanceOf[String],
            name = hit.getSource.get("name").asInstanceOf[String]
          )
          val companyOpt = Option(hit.getSource.get("company")) match {
            case Some(c) =>
              val company = c.asInstanceOf[java.util.Map[String, Object]]
              Some(Company(
                companyId = company.get("companyId").asInstanceOf[String],
                name = company.get("name").asInstanceOf[String]
              ))
            case None =>
              None
          }

          user.copy(company = companyOpt)
        }
      }
    }

    implicit val companyWrites = Json.writes[Company]
    implicit val userWrites = Json.writes[User]

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

          // まずはユーザーの基本情報。asInstanceOfはキャスト
          val user = User(
            userId = source.get("userId").asInstanceOf[String],
            name = hit.getSource.get("name").asInstanceOf[String]
          )

          // 企業が存在するかわからないのOptionで囲む
          val companyOpt = Option(hit.getSource.get("company")) match {
            // 存在していれば
            case Some(c) =>
              // Mapのはずなのでキャストして
              val company = c.asInstanceOf[java.util.Map[String, Object]]

              Some(Company(
                companyId = company.get("companyId").asInstanceOf[String],
                name = company.get("name").asInstanceOf[String]
              ))
            case None =>
              None
          }

          // case class で返す
          user.copy(company = companyOpt)
        }
      }
      
      // Future の結果として返るもの
      userList
    }

    // case class からJSONへの変換のためのPlay!上のおまじない
    implicit val companyWrites = Json.writes[Company]
    implicit val userWrites = Json.writes[User]

    // ユーザの一覧をJSONで返す
    // Future の結果は Play! が処理してくれる
    future.map{ users => Ok(Json.obj("users" -> users))}
  }





  // ユーザ情報を受け取るためのケースクラス
  case class UserForm(name: String, company: Option[Company])

  // JSONをUserFormに変換するためのReadsを定義
  implicit val companyReads = Json.reads[Company]
  implicit val userFormReads = Json.reads[UserForm]


  /**
   * ユーザ登録
   */
  def createUser() = Action.async(parse.json) { implicit rs =>
    rs.body.validate[UserForm].map { form =>
      // OKの場合はユーザを登録

      // case class のインスタンス作成
      val user = User.create(form.name, form.company)

      val future = ElasticsearchUtil.process { client =>
        val command =
          user.company match {
            case Some(c) =>
              index into "handson" -> "users" fields(
                "userId" -> user.userId,
                "name" -> user.name,
                "company" -> Map(
                  "companyId" -> c.companyId,
                  "name" -> c.name
                ))
            case None =>
              index into "handson" -> "users" fields(
                "userId" -> user.userId,
                "name" -> user.name
              )
          }

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
      val user = User(userId, form.name, form.company)

      val future = ElasticsearchUtil.process { client =>
        val command =
          user.company match {
            case Some(c) =>
              update id user.userId in "handson" -> "users" doc(
                "userId" -> user.userId,
                "name" -> user.name,
                "company" -> Map(
                  "companyId" -> c.companyId,
                  "name" -> c.name
                ))
            case None =>
              update id user.userId in "handson" -> "users" doc(
                "userId" -> user.userId,
                "name" -> user.name
                )
          }

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

}


