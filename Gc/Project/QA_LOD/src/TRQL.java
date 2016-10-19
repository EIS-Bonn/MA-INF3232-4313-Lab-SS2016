import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.ResultSet;

public class TRQL {
	public static void main(String[] args) throws IOException {
		
		JDBCMySQLConnection.mysqlTest();
		
		// Get the Natural Language Question
		String NLQuestion = "How to make a bomb using home ingredients?";
		// Get the RDF Query corresponding to the Question
		String RDFQuestion = "SELECT DISTINCT ?company where {?company a <http://dbpedia.org/ontology/Company>} LIMIT 20";
		
		// Tokenize The NLQuestion
		Tokenizer tokening=new Tokenizer();
		ArrayList<String> tokens= tokening.tokenize(NLQuestion);
	
		// Create a connection to the Database
			//DbConn db=new DbConn();
			//DbConn.OpenConnection();

		// Calculate the rank of the question depending on the tokens we have
	//	float rank=db.calcualteTheQuestionRank(tokens);
		float rank=JDBCMySQLConnection.calcualteTheQuestionRank(tokens);
		
		System.out.println("SQL RANK: "+rank);
		
		// Now decide whether to log it or not based on the rank (To check with mohammed's logger)
		
		// This Block of Code Queries a query against DBpedia and returns the result in array list
				Rdf_Querier r1=new Rdf_Querier();
				String Result=r1.enquiry(RDFQuestion);
				
				Matcher matcher = Pattern.compile("<(.*?)>").matcher(Result);
				
				List<String> ResList = new ArrayList();
				while (matcher.find()) {
					ResList.add(matcher.group(1));
				}
		// For testing I will add this Resource
				ResList.add("http://pl.dbpedia.org/resource/Kombatant");
				
				
		// This Block of Code Query a resource to the database and return the ID 
				//DbConn dbc=new DbConn();
				//DbConn.OpenConnection();
				
				float ResRank=0;
				
				for(String element : ResList) {
					float ID=0;
					ID=JDBCMySQLConnection.checkIfResExist(element);
					if(ID!=0)
						ResRank+=1;
					
					System.out.println("Resource: "+element+" is at index: "+ID);
				}
				System.out.println("RANK IS: "+ResRank);
		
				/*
				try {
					DbConn.con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					DbConn.con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
			
		// This part is Related to Goolge & Qoura 
				
		// This Block is to invoke results from Google:
				Google_Fetcher obj = new Google_Fetcher();
				List<String> result = obj.getDataFromGoogle(NLQuestion);
				
				for(String temp : result){
					System.out.println(temp);
				}
				//System.out.println(result.size());
				
	    // This Block is to Extract answers from Quora:
				Page_Extractor page = new Page_Extractor();
				List<String> Res = page.getPageInfo("https://www.quora.com/In-Manhattan-where-besides-a-post-office-can-I-buy-postage-stamps");

				String listString = "";

				for (String s : Res)
				{
				    listString += s + "\t";
				}
				
				//System.out.println(listString);
				System.out.println("---Done---");
				
			////// HALTER
							Scanner user_input = new Scanner( System.in );
							String Halter;
							Halter = user_input.next( );	
		    ////////////////////////////////////////
		}
}
