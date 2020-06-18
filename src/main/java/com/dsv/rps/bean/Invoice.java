package com.dsv.rps.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Invoice {

	private String date;
	private String invoiceNo;
	private String currency;
	private String incoterms;
	private String destinationCode;
	private String exit;
	private String shipmentId;
	private String deereOrderNo;
	private List<String> paymentTerms;
	private String shipDate;
	private Map<String, BusinessPartner> partners;
	private List<Item> items;
	private List<Packaging> packages;

	
	
	private int numberOfPackages;
	private Double grossWeight;
	private Double netWeight;
	private Double volume;
	
	
	
	// chargelines, for text version only
	private List<ChargeLine> chargeLines;
	private Map<String, Integer> chargeLinesIdx;

	public Invoice() {
		date = "";
		invoiceNo = "";
		destinationCode = "";
		currency = "";
		incoterms = "";
		shipDate = "";
		deereOrderNo = "";
		exit = "";
		shipmentId = "";

		partners = new HashMap<String, BusinessPartner>();
		items = new ArrayList<Item>();
		packages = new ArrayList<Packaging>();
		paymentTerms = new ArrayList<String>();

		chargeLines = new ArrayList<ChargeLine>();
		chargeLinesIdx = new HashMap<String, Integer>();
		
		numberOfPackages=0;
		grossWeight=Double.valueOf(0);
		netWeight=Double.valueOf(0);
		volume=Double.valueOf(0);
		 
	}

	// used for pdf
	public static Invoice getTest() {
		Invoice inv = new Invoice();

		BusinessPartner p1 = new BusinessPartner();
		p1.setName("DEERE & COMPANY");
		p1.setAdress1a("WORLWIDE LOGISTICS OPERATION");
		p1.setAdress1b(" ");
		p1.setAdress2("3400 80th Street");
		p1.setAdress3("MOLINE  IL 61025");
		p1.setAdress4("UNITED STATES");
		inv.getPartners().put("SHP", p1);

		BusinessPartner p2 = new BusinessPartner();
		p2.setName("JOHN DEERE BRASIL LTDA");
		p2.setAdress1a("RUA SERGIO FERNANDES BORGES SOARES");
		p2.setAdress1b(" DISTRITO INDUSTRIAL");
		p2.setAdress2("13054 COMPINAS-SP  ");
		p2.setAdress3("CNPJ");

		p2.setAdress4("BRAZIL");
		inv.getPartners().put("CNE", p2);

		BusinessPartner p3 = new BusinessPartner();
		p3.setName("JOHN DEERE BRASIL LTDA");
		p3.setAdress1a("RUA SERGIO FERNANDES BORGES SOARES");
		p3.setAdress1b(" DISTRITO INDUSTRIAL");
		p3.setAdress2("13054 COMPINAS-SP  ");
		p3.setAdress3("CNPJ");

		p3.setAdress4("BRAZIL");
		inv.getPartners().put("INV", p3);
		inv.setDate("20200228");
		inv.setInvoiceNo("627980");
		inv.setShipDate("20200301");

		List<String> paymentterms = new ArrayList<String>();
		paymentterms.add("term 1");
		paymentterms.add("term 2");
		inv.setPaymentTerms(paymentterms);
		
		  
		 
		ChargeLine chargeline1=new ChargeLine("AIRFREIGHT",Float.parseFloat("50.33"));
		inv.getChargeLines().add(chargeline1);
			ChargeLine chargeline2=new ChargeLine("INSURANCE FEE",Float.parseFloat("3.22"));
			inv.getChargeLines().add(chargeline2);
		
		
		
		for (int i = 0; i < 20; i++) {
			Item item1 = new Item();
			item1.setCountryOfOrigin("US");
			item1.setDescription("FITTING");
			item1.setPartNumber("62M1013");
			item1.setHsCode("7326908688");
			item1.setCustomerOrderNo("AIREXP3");
			item1.setDeereOrderNo("2003714224");
			item1.setQuantity("23");
			item1.setValue("342.01");
			inv.getItems().add(item1);

			Item item2 = new Item();
			item2.setCountryOfOrigin("US");
			item2.setDescription("FITTING PLUG");
			item1.setHsCode("7326908688");
			item2.setCustomerOrderNo("AIREXP3");
			item2.setQuantity("1");
			item2.setValue("13.47");
			inv.getItems().add(item2);

			Item item3 = new Item();
			item3.setCountryOfOrigin("US");
			item3.setDescription("BALL");
			item1.setHsCode("7326908688");
			item3.setPartNumber("F7100001D");
			item3.setCustomerOrderNo("AIREXP3");
			item3.setQuantity("3");
			item3.setValue("2.19");
			inv.getItems().add(item3);

			Item item4 = new Item();
			item4.setCountryOfOrigin("US");
			item4.setDescription("POTENTIONMETER");
			item1.setHsCode("853340807");
			item4.setCustomerOrderNo("AIREXP2");
			item4.setQuantity("7");
			item4.setValue("1034.04");
			inv.getItems().add(item4);

		}

		for (int i = 0; i < 15; i++) {
			Packaging pa1 = new Packaging();
			pa1.setCaseNumber("CS123");
			pa1.setHeight("100.00");
			pa1.setLength("80.00");

			pa1.setWidth("180.00");
			inv.getPackaging().add(pa1);
			
			Packaging pa2 = new Packaging();
			pa2.setCaseNumber("IF33");
			pa2.setHeight("10.00");
			pa2.setLength("40.00");

			pa2.setWidth(" 80.00");
			inv.getPackaging().add(pa2);
		}
		return inv;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getExit() {
		return exit;
	}

	public void setExit(String exit) {
		this.exit = exit;
	}

	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDeereOrderNo() {
		return deereOrderNo;
	}

	public void setDeereOrderNo(String deereOrderNo) {
		this.deereOrderNo = deereOrderNo;
	}

	public List<ChargeLine> getChargeLines() {
		return chargeLines;
	}

	public void setChargeLines(List<ChargeLine> chargeLines) {
		this.chargeLines = chargeLines;
	}

	public Map<String, Integer> getChargeLinesIdx() {
		return chargeLinesIdx;
	}

	public void setChargeLinesIdx(Map<String, Integer> chargeLinesIdx) {
		this.chargeLinesIdx = chargeLinesIdx;
	}

	public String getIncoterms() {
		return incoterms;
	}

	public void setIncoterms(String incoterms) {
		this.incoterms = incoterms;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	public Map<String, BusinessPartner> getPartners() {
		return partners;
	}

	public void setPartners(Map<String, BusinessPartner> partners) {
		this.partners = partners;
	}

	public List<Item> getItems() {
		return items;
	}

	public List<String> getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(List<String> paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Packaging> getPackaging() {
		return packages;
	}

	public void setPackaging(List<Packaging> packages) {
		this.packages = packages;
	}

	public List<Packaging> getPackages() {
		return packages;
	}

	public void setPackages(List<Packaging> packages) {
		this.packages = packages;
	}

	public int getNumberOfPackages() {
		return numberOfPackages;
	}

	public void setNumberOfPackages(int numberOfPackages) {
		this.numberOfPackages = numberOfPackages;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("invoiceNo=").append(this.invoiceNo).append(",date=").append(this.date).append(",deer Order No=")
				.append(this.deereOrderNo).append(",exit=").append(this.exit).append(", ship Date=")
				.append(this.shipDate).append(",shipment Id=").append(this.shipmentId);
		return sb.toString();

	}
}
