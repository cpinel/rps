package com.dsv.rps.logging;

public enum Warning {

	INVOICE_WEIGHT_EXCEEDS_SHIPMENT_WEIGHT("Sum of cases in the Invocie exceed the weight of the whole shipment"),
	
	HAWB_CHARGE_DETAILS_MISSING("HAWB charge could not be considred due to missing info"),
	
	
	;
	
	private String text;
	
	Warning(String text) {
	        this.text = text;
	    }
	 
	    public String getText() {
	        return text;
	    }
	
}