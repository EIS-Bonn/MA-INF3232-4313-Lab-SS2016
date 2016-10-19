
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

public class Google_Fetcher {
	 private static Pattern patternDomainName;
	  private Matcher matcher;
	  private static final String DOMAIN_NAME_PATTERN
		= "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
	  static {
		patternDomainName = Pattern.compile(DOMAIN_NAME_PATTERN);
	  }
	  
	  public String getDomainName(String url){

			String domainName = "";
			matcher = patternDomainName.matcher(url);
			if (matcher.find()) {
				domainName = matcher.group(0).toLowerCase().trim();
			}
			return domainName;

		  }

		  public List <String> getDataFromGoogle(String query) {

			  
			  List<String> result = new ArrayList<String>();
		
			String request = "https://www.google.com/search?q=" + query + " site:https://www.quora.com";
			System.out.println("Sending request..." + request);

			try {

				// need http protocol, set this as a Google bot agent :)
				Document doc = Jsoup
					.connect(request)
					.userAgent(
					  "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
					.timeout(5000).get();

				// get all links
				Elements links = doc.select("a[href]");
				//Elements tag = doc.getElementsByTag("h3");
				//Elements links = tag.select("a[href]");
				int counter =0 ;
				for (Element link : links) {
				
					String temp = link.attr("href");
					if(temp.startsWith("/url?q=")){
		                                //use regex to get domain name
						//result.add(getDomainName(temp));
						
					//	System.out.println("->"+temp);
						//String str = "ABC[ This is the text to be extracted ]";    
						//String Newstr = str.substring(str.indexOf("www.") + 1, str.indexOf("&sa"));
						
						String pattern1 = "://";
						String pattern2 = "&sa";
						String text = temp;

						Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
						Matcher m = p.matcher(text);
						String holder="";
						while (m.find()) {
						 // System.out.println("While: "+m.group(1));
						  holder=m.group(1);
						  }
						
						if(counter<3){
						result.add(holder);
						counter++;
						}
						else{
							return result;
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			return result;
		  }
}
