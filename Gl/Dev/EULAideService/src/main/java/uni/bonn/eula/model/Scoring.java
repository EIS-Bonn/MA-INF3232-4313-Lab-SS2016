package uni.bonn.eula.model;

public class Scoring {
	public static int score_date = 3;
	public static int score_time = 3;
	public static int score_location = 3;
	public static int score_organization = 4;
	public static int score_duration = 3;
	public static int score_person = 2;
	public static int score_money = 3;
	public static int score_number = 1;


	public static final int INDEX_MAKER = 20;
	public static final int LEVEL_1_POS = 2;
	public static final int LEVEL_2_POS = 4;
	public static final int LEVEL_3_POS = 6;
	public static void clear(){
		score_date = 3;
		score_time = 3;
		score_location = 4;
		score_organization = 4;
		score_duration = 4;
		score_person = 2;
		score_money = 3;
		score_number = 1;
	}
}
