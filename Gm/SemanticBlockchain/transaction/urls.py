

from django.conf.urls import url, include
from django.contrib import admin
from . import views


app_name = 'transaction'

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^(?P<transaction_hsh>[a-fA-F0-9]{64})$', views.transactiongraoh, name='transactionGraph')
]