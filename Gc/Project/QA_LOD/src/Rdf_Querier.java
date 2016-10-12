import org.apache.jena.rdf.model.ModelFactory;
import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.HashSet;
import java.util.List ;
import java.util.Map ;
import java.util.Set;
import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.* ;

public class Rdf_Querier {
	private String serviceURI;
	private ArrayList resources;
	private String rdfs;
	private String rdf;
	public Rdf_Querier()
	{
	 serviceURI  = "http://dbpedia-live.openlinksw.com/sparql" ;
	 resources = new ArrayList();
	 rdfs="<http://www.w3.org/2000/01/rdf-schema#>";
	 rdf="<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
	}
	public void change_serviceURL(String new_service)
	{
		serviceURI=new_service;
	}
	
	public String enquiry(String Query)
	{
		//String serviceURI  = "http://dbpedia-live.openlinksw.com/sparql" ;
		String serviceURI  = "http://dbpedia.org/sparql" ;
        String queryString = 
            "SELECT * WHERE { " +
            "    SERVICE <" + serviceURI + "> { " +
            //Your Query Goes Here:		
            Query +
            
            "    }" +
            "}" ;
        
        Query query = QueryFactory.create(queryString) ;
        String Result;
        try(QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
            Map<String, Map<String,List<String>>> serviceParams = new HashMap<String, Map<String,List<String>>>() ;
            Map<String,List<String>> params = new HashMap<String,List<String>>() ;
            List<String> values = new ArrayList<String>() ;
            values.add("2000") ;
            params.put("timeout", values) ;
            serviceParams.put(serviceURI, params) ;
            qexec.getContext().set(ARQ.serviceParams, serviceParams) ;
            ResultSet rs = qexec.execSelect() ;
       
        	Result=ResultSetFormatter.asText(rs);

        }
       return Result;
	}
	public ArrayList<String> resource_enquiry(String predicate,String object,String language)
	{
		 ParameterizedSparqlString qs = new ParameterizedSparqlString( "" +
	                "prefix rdfs:    "+rdfs+"\n" +
	                "prefix rdf:    "+rdf+"\n" +
	                "\n" +
	                "select ?resource where {\n" +
	                "  ?resource rdfs:label ?label\n" +
	                "}" ); 
		 	Literal _object= ResourceFactory.createLangLiteral( object, language );
	        qs.setParam( "label", _object );
	        Literal _predicate= ResourceFactory.createPlainLiteral(predicate);
	        qs.setParam( "rdfs:label", _predicate );
	        System.out.println( qs );
	        QueryExecution exec = QueryExecutionFactory.sparqlService( "http://dbpedia-live.openlinksw.com/sparql", qs.asQuery() );

	        // Normally you'd just do results = exec.execSelect(), but I want to 
	        // use this ResultSet twice, so I'm making a copy of it.  
	        ResultSet results = ResultSetFactory.copyResults( exec.execSelect() );
	        while ( results.hasNext() ) {
	            // don't use the `?` in the variable
	            // name here. Use *just* the name of the variable.
	        	resources.add(results.next().get( "resource" ));
	        }
	        // A simpler way of printing the results.
	        //ResultSetFormatter.out( results );
	        //to remove duplicate we use Set
	        Set<String> hs = new HashSet<String>();
	        hs.addAll(resources);
	        resources.clear();
	        resources.addAll(hs);
		
		return resources;
	}
	
	
	
	//we use thttps://www.ebi.ac.uk/rdf/querying-sparql-javahose
	//
	//http://stackoverflow.com/questions/24116853/query-sparql-to-dbpedia-using-java-code

}

