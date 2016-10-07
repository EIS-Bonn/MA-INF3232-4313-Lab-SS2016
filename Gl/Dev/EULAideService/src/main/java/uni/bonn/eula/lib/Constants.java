package uni.bonn.eula.lib;

import java.util.HashSet;

public class Constants {
	public static final String FILE_SEP = System.getProperty("file.separator");
	public static final String NEW_LINE = "\n";
	public static final String CATEGORY = "category";
	public static final String RULE = "rule";
	public static final String KIND = "kind";
	public static final String TYPE = "type";
	public static final String STRING = "string";


	public static final int DATETIME_INCR 		= 4;
	public static final int LOCATION_INCR 		= 4;
	public static final int MONEY_INCR 			= 3;
	public static final int ORG_INCR			= 3;
	public static final int DUR_INCR 			= 3;
	public static final int PERSON_INCR 		= 2;
	public static final int NUM_INCR 			= 2;


	public static final String DATE 		= "DATE";
	public static final String TIME 		= "TIME";
	public static final String LOCATION 	= "LOCATION";
	public static final String ORGANIZATION = "ORGANIZATION";
	public static final String DURATION 	= "DURATION";
	public static final String PERSON 		= "PERSON";
	public static final String NUMBER 		= "NUMBER";
	public static final String MONEY 		= "MONEY";


	public static HashSet<String> POS_ENUMS = new HashSet<String>(getEnums());


	private static HashSet<String> getEnums() {

		HashSet<String> values = new HashSet<String>();

		for (POSEnum c : POSEnum.values()) {
			values.add(c.name());
		}

		return values;
	}

}
