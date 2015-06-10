# Elasticsearch のCRUD操作

## 接続方法

Elasticsearch は2種類の通信が用意されています。

    ポート`9200`版: RESTful API
    ポート`9300`版: ネイティブトランスポートプロトコル

当ハンズオンでは、ブラウザからの操作で`9200`番を、Scalaからの操作で`9300`を利用します。
なお、`9300`ではクラスタに参加する`Node client`とリモートからアクセスする`Transport client`と
がありますが、ここでは後者で接続します。

## KOPFプラグインを利用した操作

ブラウザ上でElasticsearchのマッピングを定義します。まず、KOPFを開きます。

    http://localhost:9200/_plugin/kopf/

上部の`rest`メニューをクリックします。RESTful APIにアクセするためのコンソール画面です。

    http://localhost:9200/_plugin/kopf/#!/rest

## マッピングの登録

以下の操作を実行します。

1. パスを`/handson`にします。
2. メソッドを`POST`にします。
3. ボディ部にelaticsearch/mappings.json を開き内容をコピー&ペーストします。
4. `send`を実行し、`{ "acknowledged" : true }` が返ってくることを確認します。

以下も、同様にKOPF画面を前提に進めます。

## データの登録

- パス: /handson/companies/11
- メソッド: POST
- ボディ:

    {
        "companyId": "c11",
        "name": "BizReach"
    }

- パス: /handson/users/101
- メソッド: POST
- ボディ:

    {
        "userId": "u101",
        "name": "Naoki Takezoe",
        "company": {
            "companyId": "c11",
            "name": "BizReach"
        }
    }

## データの参照(GET)

- パス: /handson/users/101
- メソッド: GET
- ボディ:

    なし

- レスポンス:

    {
        "_index" : "handson",
        "_type" : "users",
        "_id" : "101",
        "_version" : 1,
        "found" : true,
        "_source" : {
            "userId" : "u101",
            "name" : "Naoki Takezoe",
            "company" : {
                "companyId" : "c11",
                "name" : "BizReach"
            }
        }
    }

## データの参照(POST)

- パス: /handson/users/_search
- メソッド: POST
- ボディ:

    {
        "filter": {
            "term": {
                "company.companyId": "c11"
            }
        }
    }

- レスポンス:

    {
        "took": 55,
        "timed_out": false,
        "_shards": {
            "total": 5,
            "successful": 5,
            "failed": 0
        },
        "hits": {
            "total": 1,
            "max_score": 1,
            "hits": [
                {
                    "_index": "handson",
                    "_type": "users",
                    "_id": "101",
                    "_score": 1,
                    "_source": {
                        "userId": "u101",
                        "name": "Naoki Takezoe",
                        "company": {
                            "companyId": "c11",
                            "name": "BizReach"
                        }
                    }
                }
            ]
        }
    }

## データの更新(PUSH)

- パス: /handson/users/101
- メソッド: PUT
- ボディ:

    {
        "userId": "u101",
        "name": "Naoki Takezoe Ver.2",
        "company": {
            "companyId": "c11",
            "name": "BizReach"
        }
    }

## データの削除(DELETE)

- パス: /handson/users/101
- メソッド: DELETE
- ボディ: なし
- レスポンス:

    {
        "found" : true,
        "_index" : "handson",
        "_type" : "users",
        "_id" : "101",
        "_version" : 3
    }
