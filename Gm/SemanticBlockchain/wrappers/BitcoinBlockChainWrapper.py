# -*- coding: utf-8 -*-
"""
Created on Thu Sep 15 17:11:09 2016

@author: oro252

This  is a wrapper to a rest API that Handles BLockChanin Data (TX-BTCAddress-Blocks-BlockHeaders)

"""
import os
import json
import requests
import csv

class BitcoinWrapper:
    BitcoinRestApiURL=""
    ApiAddressSuffix=""
    ApiTransactionSuffix=""
    ApiBlockSuffix=""
    Apiformat=""
    FirstBlock_in_2015 = 336861
    LastBlock_in_2015 = 391181
    FirstBlock_in_2016 = 391182
    LastBlock_in_2016 = 431135

    # Constructor with default for Blockexplorer API
    def __init__(self, BitcoinRestApiURL="https://blockexplorer.com/api/",ApiAddressSuffix="addr/",ApiTransactionSuffix="tx/",ApiBlockSuffix="block/", Apiformat=""):
        self.BitcoinRestApiURL = BitcoinRestApiURL
        self.ApiAddressSuffix = ApiAddressSuffix
        self.ApiTransactionSuffix = ApiTransactionSuffix
        self.ApiBlockSuffix = ApiBlockSuffix
    
    def get_BTCAddress(self,userinfo,isUserProfile=False):
        if(isUserProfile):
            try:
                Bitcoinexplorer_Api=self.BitcoinRestApiURL+self.ApiAddressSuffix+userinfo['profile']['bitcoin']['address']+self.Apiformat
            except:
                print "Error in the structure of "+userinfo['profile']['bitcoin']
        else:
            Bitcoinexplorer_Api="https://blockexplorer.com/api/addr/"+userinfo+self.Apiformat
        userAdressInfo=requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()
        return userAdressInfo
        
    def get_Transactions(self,userAddress):
        transactions=[]
        for transaction in userAddress['transactions']:
            Bitcoinexplorer_Api=self.BitcoinRestApiURL+self.ApiTransactionSuffix+transaction+self.Apiformat
            Transactioninfo=requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()
            transactions.append(Transactioninfo)          
        return transactions

    def get_Block_ByTransaction(self,transaction):
        Bitcoinexplorer_Api=self.BitcoinRestApiURL+self.ApiBlockSuffix+transaction['blockhash']+self.Apiformat
        blockinfo=requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()
        return blockinfo

    def get_Block(self, blockid):
        Bitcoinexplorer_Api = self.BitcoinRestApiURL + self.ApiBlockSuffix +str(blockid)+self.Apiformat
        blockinfo = requests.get(Bitcoinexplorer_Api, headers={"accept": "application/json"}).json()
        return blockinfo

    def add2015_2016(self):
        transactionslist = []
        for i in range(self.FirstBlock_in_2015, self.LastBlock_in_2016):
            block=self.get_Block(i)
            for tx in block["tx"]:
                current_tx = {}
                current_tx["hash"] = tx['hash']
                current_tx["time"] = tx['time']

                inputtx = []
                for intx in tx['inputs']:
                    current_in = {}
                    if("prev_out" in intx):
                        current_in["addr"] = intx["prev_out"]["addr"]
                        current_in["Value"] = intx["prev_out"]["value"]
                    else:
                        current_in["addr"] = "newly Generated"

                    inputtx.append(current_in)
                current_tx["senders"] = inputtx

                outputtx = []
                for outx in tx['out']:
                    current_out = {}
                    current_out["addr"] = outx['addr']
                    current_out["value"] = outx['value']
                    outputtx.append(current_out)
                current_tx["receiver"] = outputtx
                transactionslist.append(current_tx)

                with open("tx.json", 'wb') as outfile:
                    json.dump(transactionslist, outfile)
        return True


