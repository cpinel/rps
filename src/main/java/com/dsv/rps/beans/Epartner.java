package com.dsv.rps.beans;

public enum Epartner {

	SHIPPER_EXPORTER("SE","SF","SH"),
	
	INVOICE_TO("BS","ITO","IV","RE"),
	
	SHIP_TO("ST","CN"),
	
	BUYER("BY");
	
	
	private String[] codes;
	
	Epartner(String ... strings )
	{
		this.codes = strings;
	}
	 
    public String[] getCodes()
    {
        return codes;
    }
}
