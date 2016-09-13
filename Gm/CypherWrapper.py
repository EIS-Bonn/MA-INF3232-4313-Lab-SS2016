import os
import json
import requests
#import py2neo
import csv


# set up authentication parameters
#py2neo.authenticate("46.101.180.63:7474", "neo4j", "uni-bonn")

# Connect to graph and add constraints.
#neo4jUrl = os.environ.get('NEO4J_URL',"http://46.101.180.63:7474/db/data/")
#graph = py2neo.Graph(neo4jUrl)

# Add uniqueness constraints.
#graph.run("CREATE CONSTRAINT ON (q:Person) ASSERT q.id IS UNIQUE;")

# Build URL.
apiUrl = "https://api.onename.com/v1/users"
# apiUrl = "https://raw.githubusercontent.com/s-matthew-english/26.04/master/test.json"

# Send GET request.
Allusersjson = requests.get(apiUrl, headers = {"accept":"application/json"}).json()

#print(json)])
UsersDetails=[]
Adresses=[]
for username in Allusersjson['usernames']:
    usernamex= username[:-3]
    apiUrl2="https://api.onename.com/v1/users/"+usernamex+"?app-id=demo-app-id&app-secret=demo-app-secret"
    try:
        userinfo=requests.get(apiUrl2, headers = {"accept":"application/json"}).json()
        if('bitcoin' not in userinfo[usernamex]['profile']):
            continue
        else:
            UsersDetails.append(userinfo)
            break
    except:
        continue
    
def get_BTCAddress(userinfoo):
     Bitcoinexplorer_Api="https://blockexplorer.com/api/addr/"+userinfoo['profile']['bitcoin']['address']
     userAdressInfo=requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()
     return userAdressInfo
     
def get_Transactions(userAddress):
    transactions=[]
    for transaction in userAddress['transactions']:
        Bitcoinexplorer_Api="https://blockexplorer.com/api/tx/"+transaction
        TransactionInfo=requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()
        transactions.append(TransactionInfo)          
    return transactions


jsn=UsersDetails[0]
for us in jsn :
   #add_Person2Graph(jsn[us])
   Addr=get_BTCAddress(jsn[us])
   #add_Addrress2Graph(Addr)
   #rel_Person2Address(jsn[us],Addr,graph)
   txs=get_Transactions(Addr)
   print txs
   '''
   for tx in txs:
       add_Transactions2Graph(tx,graph)
       inputTx=get_input_from_tx(tx)
       add_TransactionInputTransactions2Graph(inputTx)
       add_TransactionOutputTransactions2Graph(outputTx)
       add_rel_TXINAddress(input)
       add_rel_TXOUtAddress()
   '''     
def add_Person2Graph(Person,graph):
    # Build query
    query = """
    WITH {json} as data
    UNWIND data['profile'] as q
    MERGE (person:Person {name:q['name']['formatted']}) ON CREATE
    SET person.PostalAddress = q['location']['formatted']
    """
    # Send Cypher query.
    graph.run(query,json=Person)
    return True

def add_Addrress2Graph(Addr,graph):
    # Build query
    query = """
    WITH {json} as data
    UNWIND data as q
    MERGE (btc:BTC_Address {address:q['address']}) ON CREATE 
    SET btc.balance = q['balance'],btc.totalSent = q['totalSent'],btc.totalReceived = q['totalReceived'],btc.unconfirmedBalance = q['unconfirmedBalance']
    """
    # Send Cypher query.
    graph.run(query,json=Addr)
    return True
    
def add_Transaction2Graph(tx,graph):
    # Build query
# Not done yet TODO:
    query = """
    WITH {tx} as data
    UNWIND data as q
    MERGE (tx:Transaction {hsh:q['txid']}) ON CREATE 
    SET tx.version=q['version'],tx.locktime=q['locktime'],tx.locktime=q['locktime'] = q['balance'],btc.totalSent = q['totalSent'],btc.totalReceived = q['totalReceived'],btc.unconfirmedBalance = q['unconfirmedBalance']
    """
    # Send Cypher query.
    graph.run(query,tx=tx)
    return True

def rel_TX2Address():
    # Build query
# Not done yet TODO:
    # Send Cypher query.
    return True

def rel_Person2Address(person,addr,graph):
    # Build query
    query = """
    MATCH (p:Person {name:{person}}), (b:BTC_Address {address:addr})
    CREATE (p)-[:HAS]->(b)
    """
    # Send Cypher query.
    graph.run(query,person=person,addr=addr)
    return True    




