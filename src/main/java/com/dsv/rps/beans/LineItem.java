package com.dsv.rps.beans;

public class LineItem {

	private String itemDescription;			//ProductDescription OR Comments/Text/Line1
	private Double partNumber;				//LineNumber
	private String HSCode;					//?
	private String customerOrderNumber;		//?
	private String deereOrderNumber;		//?
	private String countryOfOrigin;			//?
	private Double unitPrice;				//ItemValue
	private Double quantity;				//OrderedQuantity
	private Double weight;					//GrossWeight
	private Double value;					//ItemValue

	private String caseNumber;				//ProductId
	private Double length;					//Length
	private Double width;					//Width
	private Double height;					//Height
	private Double gw;						//GrossWeight
	private Double netweight;				//NetWeight
	private String type;					//NatureOfGoods
	
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public Double getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(Double partNumber) {
		this.partNumber = partNumber;
	}
	public String getHSCode() {
		return HSCode;
	}
	public void setHSCode(String hSCode) {
		HSCode = hSCode;
	}
	public String getCustomerOrderNumber() {
		return customerOrderNumber;
	}
	public void setCustomerOrderNumber(String customerOrderNumber) {
		this.customerOrderNumber = customerOrderNumber;
	}
	public String getDeereOrderNumber() {
		return deereOrderNumber;
	}
	public void setDeereOrderNumber(String deereOrderNumber) {
		this.deereOrderNumber = deereOrderNumber;
	}
	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}
	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getGw() {
		return gw;
	}
	public void setGw(Double gw) {
		this.gw = gw;
	}
	public Double getNetweight() {
		return netweight;
	}
	public void setNetweight(Double netweight) {
		this.netweight = netweight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
