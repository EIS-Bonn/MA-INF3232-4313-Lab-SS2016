package uni.bonn.eula.model;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class ThreadPart {
	private String senderName;
	private String senderEmail;
	private String sentTime;
	private String body;
	
	public String getSenderName(){
		return senderName;
	}
	public void setSenderName(String name){
		senderName = name;
	}
	
	public String getSenderEmail(){
		return senderEmail;
	}
	public void setSenderEmail(String email){
		senderEmail = email;
	}
	
	public String getSentTime(){
		return sentTime;
	}
	public void setSentTime(String time){
		sentTime = time;
	}
	
	public String getBody(){
		return body;
	}
	public void setBody(String message){
		this.body = message;
	}
	
}
