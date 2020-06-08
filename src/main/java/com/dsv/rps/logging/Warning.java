package com.dsv.rps.utils;

public enum Warning {

	INVOICE_WEIGHT_EXCEEDS_SHIPMENT_WEIGHT("Sum of cases in the Invocie exceed the weight of the whole shipment");
	
	
	private String text;
	
	Warning(String text) {
	        this.text = text;
	    }
	 
	    public String getText() {
	        return text;
	    }
	
}
