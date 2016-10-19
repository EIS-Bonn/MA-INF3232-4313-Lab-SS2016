import os
import json
import requests
import py2neo
import csv


def get_BTCAddress(userinfoo,user):
    if(user):
        Bitcoinexplorer_Api="https://blockexplorer.com/api/addr/"+userinfoo['profile']['bitcoin']['address']
    else:
        Bitcoinexplorer_Api="https://blockexplorer.com/api/addr/"+userinfoo
    userAdressInfo=requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()
    return userAdressInfo

def get_Transaction(hsh):
	Bitcoinexplorer_Api="https://blockexplorer.com/api/tx/"+hsh
        return requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()

def get_Transactions(userAddress):
    transactions=[]
    for transaction in userAddress['transactions']:
        Bitcoinexplorer_Api="https://blockexplorer.com/api/tx/"+transaction
        Transactioninfo=requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()
        transactions.append(Transactioninfo)
    return transactions

def relInAdresses2transaction(tx,intx,graph):
    # Build query
    query = """
	WITH {tx} as tx, {intx} as intx
    MATCH(p:BTC_Address {address:intx['addr']}), (b:Transaction {hsh:tx['txid']})
    MERGE (p)-[:Inputs_to{scriptSig:intx['scriptSig']['asm'],sequence:intx['sequence'],value:intx['value']}]->(b)
    """
    # Send Cypher query.
    graph.run(query,tx=tx,intx=intx)

def addBlocks2graph(blockinfo,graph):
    # Build query
    query = """
    WITH {blockinfo} as blockinfo
    MATCH(p:BTC_Address {address:intx['addr']}), (b:Transaction {hsh:tx['txid']})
    MERGE (p)-[:Inputs_to{scriptSig:intx['scriptSig']['asm'],sequence:intx['sequence'],value:intx['value']}]->(b)
    """
    # Send Cypher query.
    graph.run(query,blockinfo=blockinfo)



def addInputAddresses(tx,graph):
    for intx in tx['vin']:
        inAddress=intx['addr']
        addr=get_BTCAddress(inAddress,False)
        add_Addrress2Graph(addr,graph)
        relInAdresses2transaction(tx,intx,graph)
    return True

#def add_Persons2Graph(Persons,graph):
    # Build query
 #   query = """
  #  WITH {json} as data
   # UNWIND data as q
    #MERGE (person:Person {name:q['profile']['name']['formatted']}) ON CREATE
    #SET person.PostalAddress = q['profile']['location']['formatted']
    #"""
    # Send Cypher query.
   # graph.run(query,json=Persons)
   # return True


def add_Person2Graph(Person,graph):
    # Build query
    query = """
    WITH {json} as q
    MERGE (person:Person {name:q['profile']['name']['formatted']}) ON CREATE
    SET person.PostalAddress = q['profile']['location']['formatted']
    """
    # Send Cypher query.
    graph.run(query,json=Person)
    return True

def add_Addrress2Graph(Addr,graph):
    # Build query
    query = """
    WITH {json} as q
    MERGE (btc:BTC_Address {address:q['addrStr']}) ON CREATE
    SET btc.balance = q['balance'],btc.totalSent = q['totalSent'],btc.totalReceived = q['totalReceived'],btc.unconfirmedBalance = q['unconfirmedBalance']
    """
    # Send Cypher query.
    graph.run(query,json=Addr)
    return True

def add_Addrresses2Graph(Addr,graph):
    # Build query
    query = """
    WITH {json} as data
    UNWIND data as q
    MERGE (btc:BTC_Address {address:q['addrStr']}) ON CREATE
    SET btc.balance = q['balance'],btc.totalSent = q['totalSent'],btc.totalReceived = q['totalReceived'],btc.unconfirmedBalance = q['unconfirmedBalance']
    """
def add_Transaction2Graph(tx,graph):
    # Build query
    query = """
    WITH {tx} as q
    MERGE (tx:Transaction {hsh:q['txid']}) ON CREATE 
    SET tx.version=q['version'],
    tx.locktime=q['locktime'],
    tx.confirmations=q['confirmations'],
    tx.valueOut=q['valueOut'],
    tx.size=q['size'],
    tx.valueIn=q['valueIn'],
    tx.fees=q['fees']
    """
    # add time property integer
    # Send Cypher query.
    graph.run(query,tx=tx)
    return True
    # TO DO
        # rel Transaction to Block
        # rel Transaction to time tree
        # rel to Blondie graph
    # Send Cypher query.
    graph.run(query,tx=tx)
    return True

def rel_Person2Address(person,addr,graph):
    # Build query
    query = """
	WITH {person} as person
	WITH {addr} as addr
    MATCH (p:Person {name:{person}}), (b:BTC_Address {address:{addr}})
    CREATE (p)-[:HAS]->(b)
    """
    # Send Cypher query.
    graph.run(query,person=person,addr=addr)
    return True

def relOutAdresses2transaction(tx,addr,outx,graph):
    # Build query
    query = """
	WITH {tx} as tx, {outx} as outx,{addr} as addr
    MATCH(p:BTC_Address {address:addr}), (b:Transaction {hsh:tx['txid']})
    MERGE (b)-[:Outputs_to{scriptPubKey:outx['scriptPubKey']['asm'],type:outx['scriptPubKey']['type'],value:outx['value']}]->(p)
    """
    # Send Cypher query.
    graph.run(query,tx=tx,outx=outx,addr=addr)

def addOutputAddresses(tx,graph):
    for outx in tx['vout']:
        outAddresses=outx['scriptPubKey']['addresses']
        for addr in outAddresses:
            addrinfo=get_BTCAddress(addr,False)
            add_Addrress2Graph(addrinfo,graph)
            relOutAdresses2transaction(tx,addr,outx,graph)
    return True

# set up authentication parameters
py2neo.authenticate("46.101.180.63:7474", "neo4j", "uni-bonn")

# Connect to graph and add constraints.
neo4jUrl = os.environ.get('NEO4J_URL',"http://46.101.180.63:7474/db/data/")
graph = py2neo.Graph(neo4jUrl)

# Add uniqueness constraints.
graph.run("CREATE CONSTRAINT ON (q:Transaction) ASSERT q.hsh IS UNIQUE;")

# Build URL.
apiUrl = "https://api.onename.com/v1/users"
# apiUrl = "https://raw.githubusercontent.com/s-matthew-english/26.04/master/test.json"

# Send GET request.
Allusersjson = requests.get(apiUrl, headers = {"accept":"application/json"}).json()


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
            add_Person2Graph(userinfo[usernamex],graph)
            Addr=get_BTCAddress(userinfo[usernamex],True)
            add_Addrress2Graph(Addr,graph)
            rel_Person2Address(userinfo[usernamex],Addr,graph)
            txs=get_Transactions(Addr)
            for tx in txs:
                add_Transaction2Graph(tx,graph)
                addInputAddresses(tx,graph)
                addOutputAddresses(tx,graph)
                # outAddresses=addOutputAddresses(tx,graph)
                # relTransaction2OutAresses(tx,outAddresses,graph)
            break
    except:
        continue
'''
jsn=UsersDetails[0]
for us in jsn :
   add_Person2Graph(jsn[us],graph)
   Addr=get_BTCAddress(jsn[us],True)
   add_Addrress2Graph(Addr,graph)
   rel_Person2Address(jsn[us],Addr,graph)
   txs=get_Transactions(Addr)
   for tx in txs:
      add_Transaction2Graph(tx,graph)
      addInputAddresses(tx,graph)
      addOutputAddresses(tx,graph)
      # outAddresses=addOutputAddresses(tx,graph)
      # relTransaction2OutAresses(tx,outAddresses,graph)
'''
