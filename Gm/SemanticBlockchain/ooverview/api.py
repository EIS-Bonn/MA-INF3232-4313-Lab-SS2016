from django.http import HttpResponse
# from wrappers.V2CypherWrapper import get_Transaction

def addTx2graph(request):
	# transactionInfo="Unknown transaction" # in case the Blockchain api fails to retreive the tx
	# if request.method=='POST':
	# 	tx=request.POST['tx']
	# 	transactionInfo=get_Transaction(tx)
		#graph=graphwrapper()
		#addInputAddresses(transactionInfo,graph)
		#addOutputAddresses(transactionInfo,graph)
	return HttpResponse("")
