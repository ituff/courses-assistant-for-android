package com.njut.data;

public class CookieElement {
	
	private String JSESSIONID;
	private String email;
	private String sessioncode;
	public String getJSESSIONID() {
		return JSESSIONID;
	}
	public void setJSESSIONID(String jSESSIONID) {
		JSESSIONID = jSESSIONID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSessioncode() {
		return sessioncode;
	}
	public void setSessioncode(String sessioncode) {
		this.sessioncode = sessioncode;
	}
	
}
