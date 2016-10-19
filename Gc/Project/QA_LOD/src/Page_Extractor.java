import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Page_Extractor {

	public List <String>  getPageInfo(String Link) throws IOException{

		List<String> result = new ArrayList<String>();
		String html = Jsoup.connect(Link).get().html();
	
		PrintWriter out = new PrintWriter("TESTINGCRAWLING.html");
		
		out.println(html);

		out.close();
		
		
		 Document document = Jsoup.parse( new File( "TESTINGCRAWLING.html" ) , "utf-8" );
		 Elements links = document.getElementsByClass("ExpandedAnswer");
		 for (Element link : links) {
			 // String linkHref = link.attr("href");
			 // String linkText = link.text();
			//  String linkdata = link.data();
			//  String stringgg = link.toString();
			  
				//System.out.println(stringgg);
				result.add(link.toString());
			//	System.out.println("/////////////////////////////////////////");
		//	  String linke= link.
			 // int x = 1;
			}	
		 return result;
	}
}
