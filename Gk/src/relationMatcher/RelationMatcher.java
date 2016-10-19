package relationMatcher;

import java.util.LinkedList;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import relationExtractor.*;

/**
 * Matches query with relation using knowledge base indexed with elastic
 * search as source. If multiple matches are possible then all of them are
 * returned.
 * 
 * @author Victor Ouko
 */

public class RelationMatcher {
    /**
     * 
     * Call this method to obtain the relation matching. 
     * 
     * @param pattern, the string to match 
     * @param client, a default elasticsearch TransportClient
     * @return a list of all found matches
     */
	public static LinkedList<String> match(String pattern, Client client){
		
		QueryBuilder qb = QueryBuilders.matchQuery(
			    "variations",    
			     pattern   
			);
		 		
		SearchResponse response = client.prepareSearch("relations")
				   .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				   .setFetchSource(new String[]{"main_form"}, null)
				   .setQuery(qb)
				   .execute()
				   .actionGet();
        
		LinkedList<String> matches = new LinkedList<String>();
		SearchHit[] results = response.getHits().getHits();
		
		for (SearchHit hit : results) {
		  matches.add(hit.getSourceAsString());   
		}
		return matches;
	}
}
