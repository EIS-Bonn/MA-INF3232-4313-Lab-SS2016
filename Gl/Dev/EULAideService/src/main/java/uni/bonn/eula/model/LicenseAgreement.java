package uni.bonn.eula.model;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class LicenseAgreement {
	private String title;
	private ArrayList<ThreadPart> threadParts = new  ArrayList<ThreadPart>();
	private Meta meta;
	private HashSet<String> firstEmailPhrases = new HashSet<String>();
	public HashSet<String> getFirstEmailPhrases() {
		return firstEmailPhrases;
	}
	public void setFirstEmailPhrases(HashSet<String> firstEmailPhrases) {
		this.firstEmailPhrases = firstEmailPhrases;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	
	public ArrayList<ThreadPart> getThreadParts(){
		return threadParts;
	}
	public void setThreadParts(ArrayList<ThreadPart> threadParts){
		this.threadParts.clear();
		this.threadParts.addAll(threadParts);
	}
	
	public void addThreadPart(ThreadPart part){
		threadParts.add(part);
	}
	
	public void clearThreadParts(){
		threadParts.clear();
	}
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
