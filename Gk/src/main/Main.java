package main;

import java.net.InetAddress;
import java.util.LinkedList;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import helperFunctions.IndexWithElastic;
import relationExtractor.RelationExtractor;
import relationMatcher.RelationMatcher;

/**
 * 
 * Calls all modules necessary to achieve relation matching: 
 * - Creates elasticsearch client 
 * - then uses class IndexWithElastic to index relations 
 * - then uses class RelationExtractor to extract relation in input query 
 * - finally uses class RelationMatcher to match extracted relation with a base
 * form in the index
 * 
 * @author Victor Ouko
 * 
 */

public class Main {
	/**
	 * For now provide input query via RelationExtractor.extract(...) in this
	 * method and start whole program form here.
	 * 
	 * @param Args
	 */
	public static void main(String[] Args) {
		try {
			Client client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(
							InetAddress.getByName("localhost"), 9300));

			IndexWithElastic.index(client);

			String query = "Paul has studied with Adam";
			//String query = "Craig Wright lives in Brooklyn";
			System.out.println("Matching relation for query: " + query);
			String pattern = RelationExtractor.extract(query);

			System.out.println("Stanford NLP matched pattern: " + pattern);
			LinkedList<String> matches = RelationMatcher.match(pattern, client);
            
			for(String match : matches){
				System.out.println(match);
			}
			DeleteIndexResponse delete = client.admin().indices()
					.delete(new DeleteIndexRequest("relations")).actionGet();
			if (!delete.isAcknowledged()) {
				System.out.println("Index wasn't deleted");
			}
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
