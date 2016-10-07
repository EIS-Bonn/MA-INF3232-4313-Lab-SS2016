package uni.bonn.eula.lib;

public enum CategoryEnum {
	ThreadBody("Thread Body"),
	Sender("Sender"),
	Receiver("Receiver"),
	ThreadHeader("Thread Header"),
	ThreadMain("Thread Main"),
	SenderName("Sender Name"),
	FromEmail("From Email"),
	SentDate("Sent Date");
	
	private String cat;
	private CategoryEnum(String s){
		cat = s;
	}
	public String getCategory(){
		return cat;
	}
}
