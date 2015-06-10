# ユーザー一覧を実装する

先程は、Elasticsearchに接続せずにデータを返しました。次に実際にElasticsearchにアクセスしてみます。

## Elasticsearchに接続する

設定を有効化して、Elasticsearchにつながるようにします。`conf/application.conf`を開きます。
以下の部分がコメントアウトされているので外します。

    # ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~
    # Elasticsearch configuration
    # ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~ ~~~~~
    elasticsearch {
      host = "localhost"
      port = 9300
      extras {
        cluster.name = "elasticsearch"
      }
    }

Elasticsearch へのアクセスのためのヘルパークラスを既に用意しています。

1. `app/Global.scala` は起動時の初期化のための実装です。
2. `app/utils/ElasticsearchUtil.scala` は接続用のヘルパークラスです。以下のように記述します

    Elasticsearch.process { client => 
        val query = ...
        
        val response = client.execute(query)
        
        ...
    }
    
3. その他、予め、`User`と`Company`の case class を用意しています。ここでの位置づけとしては、`DTO(Data Transfer OBject)`または`Value Object`に相当します。


## コントローラを本実装する

`JsonController`の実装を本実装にします。`import`忘れにご注意ください。
完成版は[GitHub](https://github.com/bizreach/play2-elasticsearch-hands-on/blob/master/play2-hands-on-master/app/controllers/JsonController.scala)にあります。

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

  //以下略
}  
```

> **POINT**
> * [コメントなしコード](https://github.com/bizreach/play2-elasticsearch-hands-on/blob/master/play2-hands-on-master/app/controllers/JsonController.scala)があるので、そちらも参照してください
> * [elastic4s](https://github.com/sksamuel/elastic4s)のクエリDSLは、JavaのネイティブAPIをScalaでラップしたものです。RESTful APIと同等(以上？)のことが可能です。
> * 結果は、`Future`(将来結果が返ってくるであろう入れ物。詳細はGoogle it!)として返ってきます。`Future`はそのままPlay2に返却できます。
> * 他、コメントを参照。
