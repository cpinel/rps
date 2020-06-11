package com.dsv.rps.parser;

import com.dsv.rps.beans.IOutputInstance;

public class TxtGenerator implements IOutputInstance {
	
	private TxtAnalyzer ta;
	
	public TxtGenerator ( TxtAnalyzer ta )
	{
		this.ta = ta ;
	}
	
	public byte[] buildOutput()
	{
		StringBuffer sb = new StringBuffer();
		for (String line : ta.getLines())
		{
			sb.append(line);
		}
		return sb.toString().getBytes();
	}

	public String fileExtension() {
		return "TXT";
	}

}
