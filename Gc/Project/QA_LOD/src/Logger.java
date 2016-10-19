import java.util.HashMap;
import java.util.Map;

// A top-level Java class mimicking static class behavior more info roo7 lhhon sony http://stackoverflow.com/questions/7486012/static-classes-in-java
public final class Logger {
	 	private static int log_num;
	 	private static Map<String,String> questions;
	 	private Logger () { // private constructor
	 		log_num = 0;
	 		questions = new HashMap<String, String>();
	    }
	   
	    public static void setMyStaticMember(int val) {
	    	log_num = val;
	    }
	    public static int getMyStaticMember() {
	        return log_num;
	    }
	    public static void add(String question) {
	        //return myStaticMember * myStaticMember;
	    	log_num++;
	    	questions.put(Integer.toString(log_num), question);
	    }
}
