package com.dsv.rps.beans;

import java.util.List;

public class InputBean {

	private String uuid;
	private String readableTxtFile;
	private String invoiceId;
	private String orderNo;
	private String hawb;
	private List<Case> cases;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getReadableTxtFile() {
		return readableTxtFile;
	}
	public void setReadableTxtFile(String readableTxtFile) {
		this.readableTxtFile = readableTxtFile;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getHawb() {
		return hawb;
	}
	public void setHawb(String hawb) {
		this.hawb = hawb;
	}
	public List<Case> getCases() {
		return cases;
	}
	public void setCases(List<Case> cases) {
		this.cases = cases;
	}
	
	
	// @TODO here other properties that we must extract / complete
	
	
}
