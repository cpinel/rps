package com.dsv.rps.resources;

public class Constants { /* not mandatory for webapp, onlw when running as Java app */

	public static String RPS_IN_CONNECTION_STRING = new Config().getConfigValue("RPS_IN_CONNECTION_STRING");
	public static String RPS_IN_QUEUE_NAME = new Config().getConfigValue("RPS_IN_QUEUE_NAME");
	public static int ROLLING_LOGS_MAX_ROWS = Integer.parseInt(new Config().getConfigValue("ROLLING_LOGS_MAX_ROWS"));
}
