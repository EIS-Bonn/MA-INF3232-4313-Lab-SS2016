# Blockchain_Project

* [Google Drive Folder](https://drive.google.com/drive/folders/0BxVAKBzwjD0BZURMT3Bqc0czOFE):: Lab Group

* [Google Drive Folder](https://drive.google.com/drive/folders/0BxVAKBzwjD0BUVc2bW5wNFpqa2c):: Héctor Thesis

* [Conference Paper](https://www.sharelatex.com/project/5673423b30638e000860f810):: CSCUBS

* [Semantic Web on the Blockchain](https://semanticblocks.wordpress.com/): Blog by Héctor about general project concepts inter alia. 

* [Toshi Testnet3](https://drive.google.com/open?id=0B-JGczgmHmZsYVVpWVVJZDlYVkU):: Postgres Sample Data(200mb)

Command Used At D2RQ

# To Generate the Mapping file from the Relational Database (postgreSQL)

./generate-mapping -u postgres --tables blocks,transactions,blocks_transactions,address_ledger_entries,addresses,addresses_outputs,unspent_outputs,inputs,outputs --verbose --debug -o outfile.ttl jdbc:postgresql://localhost:5432/postgres

# To Generate the RDF file from the Relational Database (postgreSQL)

./generate-mapping -u postgres --tables blocks,transactions,blocks_transactions,address_ledger_entries,addresses,addresses_outputs,unspent_outputs,inputs,outputs --verbose --debug -v jdbc:postgresql://localhost:5432/postgres

# To Run the D2RQ server

./d2r-server toshi-map.ttl 