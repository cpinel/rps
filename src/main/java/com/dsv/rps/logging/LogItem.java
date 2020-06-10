package com.dsv.rps.logging;

import java.util.Date;

public class LogItem {

	private Date timestamp;
	private LogGroup group;
	private String text;

	public LogItem (Date timestamp, LogGroup group, String text)
	{
		this.group = group;
		this.timestamp = timestamp;
		this.text = text;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public LogGroup getGroup() {
		return group;
	}
	public void setGroup(LogGroup group) {
		this.group = group;
	}
	
}
