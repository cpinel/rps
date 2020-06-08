package com.dsv.rps.logging;

import java.util.ArrayList;
import java.util.Date;

import com.dsv.rps.resources.Constants;

public class RollingLogs {

	private static ArrayList<LogItem> logs;
	
	public static void addItem ( String text,LogGroup group)
	{
		if (logs==null)
			logs = new ArrayList<LogItem>();
		
		logs.add(0,new LogItem(new Date(),group,text));
		
		if (logs.size() > Constants.ROLLING_LOGS_MAX_ROWS)
			logs.remove(Constants.ROLLING_LOGS_MAX_ROWS);
	}

	public static ArrayList<LogItem> getLogs() {
		
		if (logs==null)
			logs = new ArrayList<LogItem>();
		
		return logs;
	}

	
	
}
