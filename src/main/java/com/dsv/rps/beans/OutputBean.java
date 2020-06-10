package com.dsv.rps.beans;

import java.util.ArrayList;
import java.util.List;

import com.dsv.rps.logging.Error;
import com.dsv.rps.logging.Warning;

public class OutputBean {

	private String uuid;
	private String binaryFile;
	private boolean pdf;
	private List<Error> errors;
	private List<Warning> warnings;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	public void setWarnings(List<Warning> warnings) {
		this.warnings = warnings;
	}
	
	public void addError(Error error)
	{
		if (errors == null)
			errors = new ArrayList<Error>();
		errors.add(error);
	}
	
}
