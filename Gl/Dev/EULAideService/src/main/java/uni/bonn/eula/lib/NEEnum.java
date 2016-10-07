package uni.bonn.eula.lib;

public enum NEEnum {
	DATE(4),		
	TIME(4), 		
	LOCATION(5), 	
	ORGANIZATION(5), 
	DURATION(5),
	PERSON(3), 	
	NUMBER(2), 
	MONEY(4); 	
	private int score;
	
	private NEEnum(int i){
		score = i;
	}
	public int score(){
		return score;
	}
}
