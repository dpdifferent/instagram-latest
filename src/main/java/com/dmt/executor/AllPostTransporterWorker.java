package com.dmt.executor;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.jinstagram.Instagram;

import com.dmt.spreadsheet.SpreadSheetHandler;
import com.dmt.utility.Constants;
import com.sachinhandiekar.examples.InstagramHandler;

public class AllPostTransporterWorker implements Runnable
{
	
	private Instagram instagram;
	private SpreadSheetHandler spreadSheetHandler;
	private String instagramUser = "";

	     
    public AllPostTransporterWorker(Instagram instagram, SpreadSheetHandler spreadSheetHandler, String instagramUser)
    {
        this.instagram=instagram;
        this.spreadSheetHandler=spreadSheetHandler;
        this.instagramUser=instagramUser;
    }
 
    public void run() 
    {
    	InstagramHandler instagramHandler = new InstagramHandler();
    	
    	String newWorkSheetName = spreadSheetHandler.renameSheetToBusy(Constants.SPREADSHEET_NAME, instagramUser);
    	List<LinkedHashMap<String, String>>  listMapRowData = instagramHandler.getUserAllPastData(instagram, instagramUser);
    	System.out.println("listMapRowData size = "+listMapRowData.size());
		if(listMapRowData != null && listMapRowData.size()>0)
		{
			this.spreadSheetHandler.insertExcelRowAll(Constants.SPREADSHEET_NAME, newWorkSheetName, listMapRowData);
		}
		spreadSheetHandler.renameSheetRemoveBusy(Constants.SPREADSHEET_NAME, newWorkSheetName);
    }
}
