package com.dsv.rps.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Invoice {

	String date;
	String destinationCode;
	String currency;

	String incoterms;
	String shipDate;
	Map<String, BusinessPartner> partners;
	List<Item> items;
	List<Package> packages;

	public static Invoice getTest() {
		Invoice inv = new Invoice();

		
		
		
		
		BusinessPartner p1 = new BusinessPartner();
		p1.setName("");
		p1.setAdress1a("4th avenue");

		p1.setAdress2("4003");
		p1.setAdress3("NEW YORK");
		p1.setAdress4("US");
		inv.getPartners().put("SHP", p1);
		
		
		BusinessPartner p2 = new BusinessPartner();
		p1.setName("");
		p1.setAdress1a("cuba libre street");

		p1.setAdress2("1222");
		p1.setAdress3("RIO");
		p1.setAdress4("BRAZIL");
		inv.getPartners().put("CNE", p1);
		
		
		for (int i = 0; i < 20; i++) {
			Item item1 = new Item();
			item1.setCountryOfOrigin("US");
			item1.setDescription("tractor");
			item1.setCustomerOrderNo("458439");
			item1.setQuantity("3");
			item1.setValue("12.0");
			inv.getItems().add(item1);

			Item item2 = new Item();
			item2.setCountryOfOrigin("CA");
			item2.setDescription("tractor");
			item2.setCustomerOrderNo("9932233");
			item2.setQuantity("22");
			item2.setValue("55.0");
			inv.getItems().add(item2);
			
			Item item3 = new Item();
			item3.setCountryOfOrigin("CA");
			item3.setDescription("top steel");
			item3.setCustomerOrderNo("1322221");
			item3.setQuantity("32");
			item3.setValue("525.0");
			inv.getItems().add(item2);

		}
		return inv;
	}

	public Invoice() {
		date = "";
		destinationCode = "";
		currency = "";
		incoterms = "";
		shipDate = "";
		partners = new HashMap<String, BusinessPartner>();
		items = new ArrayList<Item>();
		packages = new ArrayList<Package>();

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
