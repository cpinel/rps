package com.dsv.rps.logging;

public enum LogGroup {

	IN_QUEUE("blue"),
	PROCESS("grey"),
	OUT_QUEUE("green"),
	ERROR("red");
	
	private String color;
	
	LogGroup(String color) {
	        this.color = color;
	    }
	 
	    public String getColor() {
	        return color;
	    }
	
}
