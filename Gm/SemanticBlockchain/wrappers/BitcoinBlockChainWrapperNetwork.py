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
    
    # Constructor with default for Blockexplorer API
    def __init__(self, BitcoinRestApiURL="https://blockexplorer.com/api/",ApiAddressSuffix="addr/",ApiTransactionSuffix="tx/",ApiBlockSuffix="block/"):
        self.BitcoinRestApiURL = BitcoinRestApiURL
        self.ApiAddressSuffix = ApiAddressSuffix
        self.ApiTransactionSuffix = ApiTransactionSuffix
        self.ApiBlockSuffix = ApiBlockSuffix
    
    def get_BTCAddress(self,userinfo,isUserProfile=False):
        if(isUserProfile):
            try:
                Bitcoinexplorer_Api=self.BitcoinRestApiURL+self.ApiAddressSuffix+userinfo['profile']['bitcoin']['address']
            except:
                print "Error in the structure of "+userinfo['profile']['bitcoin']
                continue
        else:
            Bitcoinexplorer_Api="https://blockexplorer.com/api/addr/"+userinfo
        userAdressInfo=requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()
        return userAdressInfo
        
    def get_Transactions(self,userAddress):
        transactions=[]
        for transaction in userAddress['transactions']:
            Bitcoinexplorer_Api=self.BitcoinRestApiURL+self.ApiTransactionSuffix+transaction
            Transactioninfo=requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()
            transactions.append(Transactioninfo)          
        return transactions

    def get_Block(self,transaction):
        Bitcoinexplorer_Api=self.BitcoinRestApiURL+self.ApiBlockSuffix+transaction['blockhash']
        blockinfo=requests.get(Bitcoinexplorer_Api, headers = {"accept":"application/json"}).json()
        return blockinfo
  
  
