from BitcoinBlockChainWrapper import BitcoinWrapper

wrapper = BitcoinWrapper(BitcoinRestApiURL="https://blockchain.info/", ApiAddressSuffix="address/", ApiTransactionSuffix="rawtx/", ApiBlockSuffix="rawblock/", Apiformat="?format=json")
block = wrapper.add2015_2016()
print block
