package com.dsv.rps.logging;

public enum Error {

	UNREADABLE_XML("Message sent by CW1 cannot is not formatted properly"),
	NO_TXT_IN_XML("No Binary file available in the XML document"),
	XMLPARSER_ISSUE("XML Parser not configured properly"),
	WRONG_XML_FORMAT("XML could not be parsed"),
	UNSUPPORTED_EONCODING_EXCEPTION("Unsupported encoding"),
	HAWB("No HAWB could be retrieved in the file"),
	ORDER_NUMBER("No Order Number could be retrieved in the file"),
	INVOICEID("No Invoice ID could be retrieved in the file"),
	HAWB_GW_MISSING("No Gross Weight for HAWB could be retrieved in the file"),
	HAWB_GW_FORMAT("Gross Weight for HAWB does not have expected format"),
	
	LINE_GW("No line found for Invoice"),
	LINE_GW_FORMAT("Line Gross Weight for Invoice does not have expected format"),
	
	EDI_CHARGE_CURR("No currency sent bw EDI"),
	EDI_CHARGE_VALUE_WRONG_FORMAT("EDI charge has a value which is not a number"),
	
	EDI_CHARGE("Could not extract EDI charges"),

	HAWB_CHARGE_VALUE_WRONG_FORMAT("HAWB charge has a value which is not a number"),

	;
	
	
	private String text;
	
	Error(String text) {
	        this.text = text;
	    }
	 
	    public String getText() {
	        return text;
	    }
	
}