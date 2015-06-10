# Elasticsearch の概念

[Hello! Elasticsearch.](https://medium.com/hello-elasticsearch) にお世話になりましょう。


## 論理的なインデクス

Index、Type、Document。(強引ですが)RDBのスキーマ、テーブル、行データに相当します

![Hello! Elasticsearch.より](https://d262ilb51hltx0.cloudfront.net/max/924/1*oP20tJyA_eDOycJPyDYo2A.png)

> [Elasticsearch システム概要](https://medium.com/hello-elasticsearch/elasticsearch-afd52d72711)より。


## 物理的なインデクス

Cluster、Node、Shard。Elasticsearchは既定でこれらが組み込まれています。アベイラビリティやスケーラビリティを容易に高めることができます。
当ハンズオンでは割愛します。

![Hello! Elasticsearch.より](https://d262ilb51hltx0.cloudfront.net/max/1001/1*m3zalHtwZeQtJa1Np1afrQ.png)

> [Elasticsearch システム概要](https://medium.com/hello-elasticsearch/elasticsearch-afd52d72711)より。


## マッピング

マッピングとは、

> Elasticsearch におけるマッピングとは、リレーショナルDBでいうところのテーブル定義に相当します。しかし、単にデータを格納する為の
> フィールドを用意して型を設定するだけではありません。Elasticsearch では、フィールドの型の他に言語解析処理などのドキュメントを検索可能にする為の各種設定が可能です。

> スキーマーレスが一つの特徴の Elasticsearch では、ドキュメントをインデックスすると自動的に各フィールド毎にフィールドタイプなどの
> マッピングが自動で設定されインデックスが作成されます。また、事前にマッピングを設定可能な仕組みとなっています。

> データの内容によって、自動でマッピングされる型や解析処理仕様などで検索要件を満たせていればこの自動マッピングの恩恵を受けることができますが、
> 経験上ほとんどの場合マニュアルでのマッピング定義が必要になるのではないかと思います。

事前にマッピングを定義します。

> [Elasticsearch マッピング](https://medium.com/hello-elasticsearch/elasticsearch-9a8743746467)


# Elasticsearch をどこで使うか？

1. フルテキスト検索インデクスとして
2. NoSQLデータベースとして


## NoSQLデータベース？

1. トランザクションをどうするか？ (ない、反映も非同期)
2. リレーションをどうするか？ (ない、包含する？リンクする？)
3. ...
