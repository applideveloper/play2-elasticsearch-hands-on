Elasticsearch のダウンロード

2015/6/5時点では、`1.5.2`が最新です。

    $ curl -o workspace/elasticsearch-1.5.2.zip https://download.elastic.co/elasticsearch/elasticsearch/elasticsearch-1.5.2.zip
    $ cd workspace
    $ unzip elasticsearch-1.5.2.zip

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

ブラウザ等で以下にアクセスしてみて下さい。

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