package com.dsv.rps.beans;

public class EDICharge {
	
	private String code;
	private String text;
	private Double value;

	public EDICharge(String code,String text, Double value)
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
