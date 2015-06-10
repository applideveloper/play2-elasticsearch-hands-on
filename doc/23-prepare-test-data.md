# Elasticsearchテストデータの準備

## RESTful APIを経由してのデータ投入

curl のコマンドを用意しています。KOPFから実行してただくことも可能です。`elasticsearch`ディレクトリ配下にあります。

とりあえずリセット
   
    $ ./drop_indicies.sh

マッピングの追加。マッピングの追加はあってもなくても大丈夫です。

    $ ./add_mappings.sh

データの追加

    $ ./add_data.sh
