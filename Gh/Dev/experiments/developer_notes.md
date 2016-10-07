Some notes for developers:
We used Waterloo datasets for our evaluations, you can find them here: http://dsg.uwaterloo.ca/watdiv/
There is a tool to generate sample datasets, you may also use already generated datasets (which we used for our evaluation). 
We used Apache web server on a MacBook Pro (Retina, 15-inch, Late 2013, 2 GHz Intel Core i7, 8 GB 1600 MHz DDR3) to run our experiments. 
For each library there is a separate .js file, you can simply call the function to run the experiment. There is a timer in the beginning of each file, and it stops at the end of processing. The duration of this period is our metric to evaluate how good is a library.
In order to have results for different queries, we have three queries for each library, simple and two complex queries. If you want, you can have all three or run them one by one.

Recommendations:
It’s not easy to decide which library is the best for running SPARQL queries on the client side, but to our knowledge rdflib is performing pretty good, and it’s also stable. Moreover, there are frequent updates on it. 
There is no need to say, but no one is willing to have such intensive tasks on the client side. We have evaluate over 10,000 records in our experiments, which is almost non sense in the client side. All libraries are performing good in a reasonable amount of records in client side (100 records).