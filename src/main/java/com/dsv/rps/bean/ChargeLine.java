package com.dsv.rps.bean;
import org.apache.log4j.Logger;

public class ChargeLine {
	private static int MAX_LENGTH=106;
	static Logger logger = Logger.getLogger(ChargeLine.class.getName());
	String rawLine = "";
	int lineIndex = -1;
	private String text = "";
	private Float value;

	public ChargeLine(String _rawline, int _lineindex) {
		 
		 
		rawLine=_rawline;
		lineIndex = _lineindex;

		try {
			if (rawLine.length() > 80) {
				text = rawLine.substring(20, 80).trim();
				String svalue = rawLine.substring(80, rawLine.length()).trim();

				value = Float.parseFloat(svalue);

			} else {

				text = rawLine.trim();
				value = Float.valueOf(0);
			}
			logger.info("Line=" + lineIndex + ",length=" + rawLine.length() + ",text=" + text + ",value=" + value);

		} finally {
		}
		rawLine= completeRightWithBlank(_rawline,MAX_LENGTH);
	 
		reconstructRawLine();
		logger.info("["+rawLine+"]");
	}

	public ChargeLine(String _rawline, int _lineindex, String _text, Float _value) {
		rawLine = _rawline;
		lineIndex = _lineindex;
		text = _text;
		value = _value;

	}
	private String completeRightWithBlank(String inputString, int length) {
	    if (inputString.length() >= length) {
	        return inputString;
	    }
	    StringBuilder sb = new StringBuilder();
	    sb.append(inputString);
	    while (sb.length() < length  ) {
	        sb.append(' ');
	    }
	     
	 
	    return sb.toString();
	}
	
	
	public void reconstructRawLine() {
		String svalue = String.format("%.2f",value); 
		 
		rawLine=rawLine.substring(0, rawLine.length()-svalue.length())+svalue;
	}
	
	public String getRawLine() {
		return rawLine;
	}

	public void setRawLine(String rawLine) {
		this.rawLine = rawLine;
	}

	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String toString() {
		return "lineindex=" + lineIndex + ", text=" + text + ", value=" + value;

	}

}
