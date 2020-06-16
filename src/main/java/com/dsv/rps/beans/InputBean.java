package com.dsv.rps.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.dsv.rps.utils.StringUtils;

public class InputBean {

	private String orderNo;
	
	private String invoiceId;
	private String hawb;
	private String readableTxtFile;
	private Double invoiceCW;
	private Double hawbCW;
	
	private String shipmentId;
	
	private HashMap<String,EDICharge> ediCharges;
	private String ediCurrency;
	
	private HAWBCharge freightHAWBCharge;
	private HashMap<String,HAWBCharge> otherHAWBCharges;
	
	private List<String> paymentTerms;
	
	private List<LineItem> lineItems;
	
	private ArrayList<Partner> partners;
	
	private int legsCount;
	
	public int getLegsCount() {
		return legsCount;
	}

	public void setLegCount(int i){
		legsCount = i;
	}

	public String getInvoiceId() {
		return invoiceId;
	}
	
	public Double getInvoiceCW() {
		return invoiceCW;
	}



	public void setInvoiceCW(Double invoiceCW) {
		this.invoiceCW = invoiceCW;
	}



	public Double getHawbCW() {
		return hawbCW;
	}


	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public void setHawbCW(Double hawbCW) {
		this.hawbCW = hawbCW;
	}



	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getHawb() {
		return hawb;
	}

	public void setHawb(String hawb) {
		this.hawb = hawb;
	}

	public String getReadableTxtFile() {
		return readableTxtFile;
	}

	public void setReadableTxtFile(String readableTxtFile) {
		this.readableTxtFile = readableTxtFile;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public List<LineItem> getLineItems() {
		if (lineItems == null)
			lineItems = new ArrayList<LineItem>();
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
	public void addLineItem ( LineItem item)
	{
		getLineItems().add(item);
	}

	public String getEdiCurrency() {
		return ediCurrency;
	}

	public void setEdiCurrency(String ediCurrency) {
		this.ediCurrency = ediCurrency;
	}

	public EDICharge getEdiCharge(String key)
	{
		if (ediCharges== null)
			ediCharges = new HashMap<String, EDICharge>();
		return ediCharges.get(key);
	}
	
	public Set<String> getEdiChargesKeys()
	{
		return ediCharges.keySet();
	}

	public void addEdiCharge(EDICharge ediCharge)
	{
		if (ediCharges== null)
			ediCharges = new HashMap<String, EDICharge>();
		this.ediCharges.put(ediCharge.getCode(),ediCharge);
	}
	
	
	public HAWBCharge getFreightHAWBCharge() {
		return freightHAWBCharge;
	}

	public void setFreightHAWBCharge(HAWBCharge freightHAWBCharge) {
		this.freightHAWBCharge = freightHAWBCharge;
	}

	public HashMap<String, HAWBCharge> getOtherHAWBCharges() {
		return otherHAWBCharges;
	}

	public void addOtherHAWBCharge(HAWBCharge hawbCharge)
	{
		if (otherHAWBCharges== null)
			otherHAWBCharges = new HashMap<String, HAWBCharge>();
		this.otherHAWBCharges.put(hawbCharge.getCode(),hawbCharge);
	}

	public String toString()
	{
		String res = "HAWB : " + getHawb() + ", Invoice Id : " + getInvoiceId() + ", order number : " + getOrderNo() + 
				 ", Invoice CW : " + getInvoiceCW() + ", HAWB CW : " + getHawbCW() + "\n";
		
		if (ediCharges!=null)
			for (EDICharge charge:ediCharges.values())
			{
				res += " EDI charge " + charge.getCode() + " : " + charge.getText() + " = " + charge.getValue();
			}
		
		res += "\nEDI Currency :" + ediCurrency;
		res += "\nHAWB Freight Charges : " + freightHAWBCharge.getCode() + " : " + freightHAWBCharge.getText() + " = " + freightHAWBCharge.getValue();
		
		if (otherHAWBCharges!=null)
			for (HAWBCharge charge:otherHAWBCharges.values())
			{
				res += "\nOther Freight Charge " + charge.getCode() + " : " + charge.getText() + " = " + charge.getValue();
			}
		
		res += "\nStages count : " + legsCount; 
			
		return res;
		
	}
	
	public void addPaymentTerm(String s)
	{
		getPaymentTerms().add(s);
	}
	
	// see COM code, for PDF
	public String getIncoterms()
	{
		if (ediCharges.get("I132")!=null)
			return ediCharges.get("I132").getText();
		return "";
	}
	
	// see COM code, for PDF
	public String getExit()
	{

		if (ediCharges.get("N1LG")!=null)
			return ediCharges.get("N1LG").getText();
		return "";
	}
	
	// see COM code, for PDF
	public String getShipMentId()
	{
		return getHawb();
	}
	
	public List<String> getPaymentTerms()
	{
		if (paymentTerms==null)
			paymentTerms = new ArrayList<String>();
		return paymentTerms;
	}
	
	public ArrayList<String> getI132()
	{
		return null; // TODO
	}
	
	// see COM code, for PDF
	public String getShipentId()
	{
		return shipmentId;
	}

	public HashMap<String, EDICharge> getEdiCharges() {
		return ediCharges;
	}

	public void setEdiCharges(HashMap<String, EDICharge> ediCharges) {
		this.ediCharges = ediCharges;
	}

	public ArrayList<Partner> getPartners() {
		if (partners==null)
			partners = new ArrayList<Partner>();
		return partners;
	}

	public void addPartner(Partner e ) {
		getPartners().add(e);
	}

	public String getShipmentId() {
		return shipmentId;
	}

	public void setOtherHAWBCharges(HashMap<String, HAWBCharge> otherHAWBCharges) {
		this.otherHAWBCharges = otherHAWBCharges;
	}

	public void setPaymentTerms(List<String> paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	
	public Partner getPartner( Epartner epartner)
	{
		for (String code : epartner.getCodes())
		{
			for (Partner partner:getPartners())
			{
				if (code.equals(partner.getRole()))
						return partner;
			}
		}
		return null;
	}

	public void setLegsCount(int legsCount) {
		this.legsCount = legsCount;
	}
	
}
