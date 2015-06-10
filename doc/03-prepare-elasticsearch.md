# Elasticsearch の準備

## Elasticsearch のダウンロード

2015/6/5時点では、`1.5.2`が最新です。

    $ cd /anywhere/you/want/to/go
    $ curl -o https://download.elastic.co/elasticsearch/elasticsearch/elasticsearch-1.5.2.zip
    $ unzip elasticsearch-1.5.2.zip

## Elasticsearch の起動

起動します。起動スクリプトをキックするだけです。

    $ cd elasticsearch-1.5.2
    $ bin/elasticsearch

以下の様なログが表示されます。

    [11:44:54 elasticsearch-1.5.2]$ bin/elasticsearch
    [2015-06-08 11:45:34,304][INFO ][node                     ] [Vashti] version[1.5.2], pid[77375], build[62ff986/2015-04-27T09:21:06Z]
    [2015-06-08 11:45:34,304][INFO ][node                     ] [Vashti] initializing ...
    [2015-06-08 11:45:34,310][INFO ][plugins                  ] [Vashti] loaded [], sites []
    [2015-06-08 11:45:37,223][INFO ][node                     ] [Vashti] initialized
    [2015-06-08 11:45:37,224][INFO ][node                     ] [Vashti] starting ...
    [2015-06-08 11:45:37,404][INFO ][transport                ] [Vashti] bound_address {inet[/0:0:0:0:0:0:0:0:9300]}, publish_address {inet[/10.199.30.108:9300]}
    [2015-06-08 11:45:37,429][INFO ][discovery                ] [Vashti] elasticsearch/wlF7T2rYSF-gmDHaGp5QRg
    [2015-06-08 11:45:41,212][INFO ][cluster.service          ] [Vashti] new_master [Vashti][wlF7T2rYSF-gmDHaGp5QRg][BIZ2014-MAC0011.local][inet[/10.199.30.108:9300]], reason: zen-disco-join (elected_as_master)
    [2015-06-08 11:45:41,228][INFO ][http                     ] [Vashti] bound_address {inet[/0:0:0:0:0:0:0:0:9200]}, publish_address {inet[/10.199.30.108:9200]}
    [2015-06-08 11:45:41,229][INFO ][node                     ] [Vashti] started
    [2015-06-08 11:45:41,244][INFO ][gateway                  ] [Vashti] recovered [0] indices into cluster_state

cURLしてみましょう。

    $ curl http://localhost:9200

    {
      "status": 200,
      "name": "Vashti",
      "cluster_name": "elasticsearch",
      "version": {
        "number": "1.5.2",
        "build_hash": "62ff9868b4c8a0c45860bebb259e21980778ab1c",
        "build_timestamp": "2015-04-27T09:21:06Z",
        "build_snapshot": false,
        "lucene_version": "4.10.4"
      },
      "tagline": "You Know, for Search"
    }


## プラグインのインストール

次にプラグインをインストールします。とりあえず便利なツールを多く含む[KOPF](https://github.com/lmenezes/elasticsearch-kopf)

    $ bin/plugin --install lmenezes/elasticsearch-kopf

    [18:44:52 elasticsearch-1.5.2]$ bin/plugin --install lmenezes/elasticsearch-kopf
    -> Installing lmenezes/elasticsearch-kopf...
    Trying https://github.com/lmenezes/elasticsearch-kopf/archive/master.zip...
    Downloading ........................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................DONE
    Installed lmenezes/elasticsearch-kopf into /Users/satoshi.kobayashi/workspace/github/bizreach/play2-elasticsearch-hands-on/elasticsearch/workspace/elasticsearch-1.5.2/plugins/kopf
    Identified as a _site plugin, moving to _site structure ...

※ プラグインによって、再起動が必要な場合と不要な場合があるようです(?)。

## 動作チェック

    http://localhost:9200/_status
    http://localhost:9200/_cat
    http://localhost:9200/_mapping
