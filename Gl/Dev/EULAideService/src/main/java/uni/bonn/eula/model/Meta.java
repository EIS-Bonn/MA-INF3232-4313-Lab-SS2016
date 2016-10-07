package uni.bonn.eula.model;

import java.util.HashSet;

public class Meta {
	private HashSet<String> emailList = null;
	private HashSet<String> placeList = null;
	private HashSet<String> dateTimeList = null;
	private HashSet<String> moneyList = null;
	private HashSet<String> urlList = null;
	private HashSet<String> peopleList = null;
	public HashSet<String> getEmailList() {
		return emailList;
	}
	public void setEmailList(HashSet<String> emailList) {
		this.emailList = emailList;
	}
	public HashSet<String> getPlaceList() {
		return placeList;
	}
	public void setPlaceList(HashSet<String> placeList) {
		this.placeList = placeList;
	}
	public HashSet<String> getDateTimeList() {
		return dateTimeList;
	}
	public void setDateTimeList(HashSet<String> dateTimeList) {
		this.dateTimeList = dateTimeList;
	}
	public HashSet<String> getMoneyList() {
		return moneyList;
	}
	public void setMoneyList(HashSet<String> moneyList) {
		this.moneyList = moneyList;
	}
	public HashSet<String> getUrlList() {
		return urlList;
	}
	public void setUrlList(HashSet<String> urlList) {
		this.urlList = urlList;
	}
	public HashSet<String> getPeopleList() {
		return peopleList;
	}
	public void setPeopleList(HashSet<String> peopleList) {
		this.peopleList = peopleList;
	}
	
	
}
