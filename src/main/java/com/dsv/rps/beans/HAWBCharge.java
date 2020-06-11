package com.dsv.rps.beans;

public class HAWBCharge {
	
	private String code;
	private String text;
	private Double value;

	public HAWBCharge(String code,String text, Double value)
	{
		this.code = code;
		this.text = text;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public Double getValue() {
		return value;
	}
	
	
}
