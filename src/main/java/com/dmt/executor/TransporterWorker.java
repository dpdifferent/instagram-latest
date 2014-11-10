package com.dmt.executor;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.jinstagram.Instagram;

import com.dmt.spreadsheet.SpreadSheetHandler;
import com.dmt.utility.Constants;
import com.sachinhandiekar.examples.InstagramHandler;
public class TransporterWorker implements Runnable
{
	
	private Instagram instagram;
	private SpreadSheetHandler spreadSheetHandler;
	private String instagramUser = "";

	     
    public TransporterWorker(Instagram instagram, SpreadSheetHandler spreadSheetHandler, String instagramUser)
    {
        this.instagram=instagram;
        this.spreadSheetHandler=spreadSheetHandler;
        this.instagramUser=instagramUser;
        System.out.println("transporter worker thread " + this + " is created for user "+instagramUser);
    }
 
    public void run() 
    {
    	InstagramHandler instagramHandler = new InstagramHandler();
    	System.out.println("\n\nThread "+this+" invoked for user " + instagramUser + " to get recent data");
    	int noOfRows = spreadSheetHandler.getNoOfRows(Constants.SPREADSHEET_NAME, instagramUser);
    	if(instagramUser.indexOf("*")<=0 && noOfRows > 0)
		{
    		long lastPostTime = spreadSheetHandler.getLastRowPostTime(Constants.SPREADSHEET_NAME, instagramUser);
    		List<LinkedHashMap<String, String>>  listMapRowData = instagramHandler.getUserRecentUpdates(instagram, instagramUser, lastPostTime);
	    	System.out.println("listMapRowData size = "+listMapRowData.size());
			if(listMapRowData != null && listMapRowData.size()>0)
			{
				this.spreadSheetHandler.insertExcelRowRecent(Constants.SPREADSHEET_NAME, instagramUser, listMapRowData);
			}
		}
    }
}
