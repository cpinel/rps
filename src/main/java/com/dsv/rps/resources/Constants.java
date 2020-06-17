package com.dsv.rps.resources;

public class Constants { /* not mandatory for webapp, onlw when running as Java app */

	public static String RPS_IN_CONNECTION_STRING = new Config().getConfigValue("RPS_IN_CONNECTION_STRING");
	public static String RPS_IN_QUEUE_NAME = new Config().getConfigValue("RPS_IN_QUEUE_NAME");

	public static String RPS_OUT_CONNECTION_STRING = new Config().getConfigValue("RPS_OUT_CONNECTION_STRING");
	public static String RPS_OUT_QUEUE_NAME = new Config().getConfigValue("RPS_OUT_QUEUE_NAME");

	
	public static int ROLLING_LOGS_MAX_ROWS = Integer.parseInt(new Config().getConfigValue("ROLLING_LOGS_MAX_ROWS"));
	
	
	
	
	public static final String MY_TEST_FILE = "C:\\Users\\didie\\Desktop\\test rps\\SHSV0000019.xml";
	
}
