package com.dsv.rps.beans;

import java.util.ArrayList;
import java.util.List;

import com.dsv.rps.logging.Error;
import com.dsv.rps.logging.Warning;

public class OutputBean {

	private String orderNumber;
	private String invoiceNumber;
	private String buyerId;
	private String binaryFile;
	private boolean pdf;
	private List<Error> errors;
	private List<Warning> warnings;
	
	private List<EDICharge> chargesToComplete;
	
	
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
	public void setWarnings(List<Warning> warnings) {
		this.warnings = warnings;
	}
	public String getBinaryFile() {
		return binaryFile;
	}
	public void setBinaryFile(String binaryFile) {
		this.binaryFile = binaryFile;
	}
	public boolean isPdf() {
		return pdf;
	}
	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}
	public List<Error> getErrors() {
		return errors;
	}
	public List<Warning> getWarnings() {
		return warnings;
	}
	
	public void addError(Error error)
	{
		if (errors == null)
			errors = new ArrayList<Error>();
		errors.add(error);
	}
	
	public void addWarning(Warning warning)
	{
		if (warnings == null)
			warnings = new ArrayList<Warning>();
		warnings.add(warning);
	}
	public List<EDICharge> getChargesToComplete() {
		if (chargesToComplete == null)
			chargesToComplete = new ArrayList<EDICharge>();
		return chargesToComplete;
	}
	public void addChargeToComplete(EDICharge chargeToComplete)
	{
		getChargesToComplete().add(chargeToComplete);
	}
	
	public String getFilename()
	{
		return orderNumber + (pdf?".pdf":".txt");
	}
}
