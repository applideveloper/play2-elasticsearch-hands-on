eval curl -f -v -XPOST 'http://localhost:9200/_bulk' --data-binary '@data_users.json'
eval curl -f -v -XPOST 'http://localhost:9200/_bulk' --data-binary '@data_companies.json'

