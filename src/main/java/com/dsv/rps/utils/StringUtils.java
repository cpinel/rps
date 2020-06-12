package com.dsv.rps.utils;

public class StringUtils {

	public static boolean isNull( String s)
	{
		return s == null || s.trim().length()==0;
	}
	
	public static boolean isInList(String value, String ...strings)
	{
		for (String string:strings)
		{
			if (string.equals(value))
				return true;
		}
		return false;
	}
}
