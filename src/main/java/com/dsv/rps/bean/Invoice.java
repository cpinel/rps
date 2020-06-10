package com.dsv.rps.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Invoice {

	String date;
	String invoiceNo;
	String currency;
	String incoterms;
	String destinationCode;
	String exit;
	String shipmentId;
List<String> paymentTerms;
	String shipDate;
	Map<String, BusinessPartner> partners;
	List<Item> items;
	List<Package> packages;

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
		
		List<String> paymentterms=new ArrayList<String>();paymentterms.add("term 1");paymentterms.add("term 2");
		inv.setPaymentTerms(paymentterms);
		
		
		
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
		return inv;
	}

	public Invoice() {
		date = "";
		invoiceNo = "";
		destinationCode = "";
		currency = "";
		incoterms = "";
		shipDate = "";

		exit = "";
		shipmentId = "";
		partners = new HashMap<String, BusinessPartner>();
		items = new ArrayList<Item>();
		packages = new ArrayList<Package>();
		paymentTerms=new ArrayList<String>();
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

	public List<Package> getPackages() {
		return packages;
	}

	public void setPackages(List<Package> packages) {
		this.packages = packages;
	}

}
