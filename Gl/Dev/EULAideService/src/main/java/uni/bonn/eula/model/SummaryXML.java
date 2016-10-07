package uni.bonn.eula.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="summary")
public class SummaryXML implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Meta meta;
	private ArrayList<String> permissions;
	private ArrayList<String> prohibitions;
	private ArrayList<String> duties;
	private String subject;
	
	public String getSubject() {
		return subject;
	}
	@XmlElement(name="subject")
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public ArrayList<String> getPermissions() {
		return permissions;
	}
	@XmlElement(name="permissions")
	public void setPermissions(ArrayList<String> permissions) {
		this.permissions = permissions;
	}
	public ArrayList<String> getProhibitions() {
		return prohibitions;
	}
	@XmlElement(name="prohibitions")
	public void setProhibitions(ArrayList<String> prohibitions) {
		this.prohibitions = prohibitions;
	}
	public ArrayList<String> getDuties() {
		return duties;
	}
	@XmlElement(name="duties")
	public void setDuties(ArrayList<String> duties) {
		this.duties = duties;
	}

	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

}
