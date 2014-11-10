package com.dmt.utility;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Hashtable;

public class Utils
{
	/**
	 * This method converts the input string into a number and returns it
	 * 
	 * @param value
	 *            String value which is to be converted into number or integer
	 * @return int Integer converted string
	 */
	public static int parseInt(Object obj)
	{
		int numValue = 0;
		try
		{
			if (obj != null && obj.toString().trim().length() > 0)
			{
				numValue = Integer.parseInt(obj.toString().trim());
			}
		}
		catch (NumberFormatException nfe)
		{
			System.out.println("common.Utils.java ::Exception while converting a string value to integer");
			nfe.printStackTrace();
			return numValue;
		}
		return numValue;
	}
	
	public static Long parseLong(Object obj)
	{
		Long numValue = 0L;
		try
		{
			if (obj != null && obj.toString().trim().length() > 0)
			{
				numValue = Long.parseLong(getPlainString(obj.toString().trim()));
			}
		}
		catch (NumberFormatException nfe)
		{
			System.out.println("common.Utils.java ::Exception while converting a string value to Long");
			nfe.printStackTrace();
			return numValue;
		}
		return numValue;
	}

	public static double parseDouble(Object obj)
	 {
	  double numValue = 0.0;
	  try
	  {
	   if (obj != null && obj.toString().trim().length() > 0)
	   {
	    numValue = Double.parseDouble(obj.toString().trim());
	   }
	  }
	  catch (NumberFormatException nfe)
	  {
	   System.out.println("common.Utils.java ::Exception while converting a string value to integer");
	   nfe.printStackTrace();
	   return numValue;
	  }
	  return numValue;
	 }
	
	/**
	 * @param list
	 * @return
	 */
	public static StringBuffer getStringBufferForDB(ArrayList<String> list)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		ArrayList<String> cloneList = (ArrayList<String>) list.clone();
		if (list != null)
		{
			int i = 0;
			for (String tempStr : cloneList)
			{
				if (tempStr.contains("\""))
					tempStr = tempStr.replace("\"", "\\\"");
				if (tempStr.contains(","))
					tempStr = tempStr.replace(",", "\\,");
				if (i < cloneList.size() - 1)
					sb.append("\"" + tempStr + "\",");
				else
					sb.append("\"" + tempStr + "\"");
				i++;
			}
			sb.append("}");
		}
		return sb;
	}
	
	/**
	 * This method returns the value of the parameter to a zero length string
	 * from null value
	 * 
	 * @param parameter
	 *            String containing parameter
	 * @return String
	 */
	public static String checkNullValueForString(Object obj)
	{
		return ((obj == null) ? "" : obj.toString().trim());
	}
	
	/**
	 * This method returns the value of the parameter to a zero value from null
	 * value
	 * 
	 * @param parameter
	 *            String containing parameter
	 * @return String
	 */
	public static String checkNullValueForInt(Object obj)
	{
		String retVal = "0";
		if (obj == null)
		{
			retVal = "0";
		}
		else
		{
			retVal = obj.toString();
			if (retVal.length() == 0)
			{
				retVal = "0";
			}
		}
		return retVal;
	}
	
	public static String checkNullValueForDouble(Object obj)
	{
		
		String retVal = "0.00";
		if (obj == null)
		{
			retVal = "0.00";
		}
		else
		{
			retVal = obj.toString();
			if (retVal.length() == 0)
			{
				retVal = "0.00";
			}
		}
		return retVal;
	}
	
	public static String getPlainString(Object obj)
	{
		String retString = "";
		if(obj  != null)
		{
			retString = obj.toString().trim();
			retString = retString.replaceAll(" ","");
			retString = retString.replaceAll("_","");
			retString = retString.replaceAll(",","");
		}
		return retString;
	}
}
