# ユーザー一覧を実装する

先程は、Elasticsearchに接続せずにデータを返しました。次に実際にElasticsearchにアクセスしてみます。

## Elasticsearchに接続する

## コントローラを本実装する

`controllers`パッケージに`JsonController`オブジェクトを以下のように作成します。

```scala
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
  def list = Action.async { implicit rs =>
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

  //以下略
}  
```
