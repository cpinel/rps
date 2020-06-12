package com.dsv.rps.beans;

public class EDICharge {
	
	private String code;
	private String text;
	private Double value;
	private Double updatedValue;

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
	
	public void updateValue(double newvalue)
	{
		this.updatedValue = newvalue;
	}
	public Double getUpdatedValue()
	{
		if (updatedValue != null)
			return updatedValue;
		
		return value;
	}
	
}
