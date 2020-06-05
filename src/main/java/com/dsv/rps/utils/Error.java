package com.dsv.rps.utils;

public enum Error {

	UNREADABLE_XML("Message sent by CW1 cannot is not formatted properly");
	
	
	private String text;
	
	Error(String text) {
	        this.text = text;
	    }
	 
	    public String getText() {
	        return text;
	    }
	
}
