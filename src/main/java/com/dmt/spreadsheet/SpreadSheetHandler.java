package com.dmt.spreadsheet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.dmt.utility.Constants;
import com.dmt.utility.CryptoUtil;
import com.dmt.utility.Utils;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.sachinhandiekar.examples.InstagramUtils;

public class SpreadSheetHandler 
{
	private SpreadsheetService service;
	private List<SpreadsheetEntry> spreadsheets;
	
	public SpreadSheetHandler()
	{
		initialize();
	}
	
	public void initialize()
	{
		try
		{
			System.out.println("initialization of SheetHanlder starts");
			
			System.out.println("getting SpreadsheetService object");
			service = new SpreadsheetService("MySpreadsheetIntegration-v1");
			System.out.println("Authenticating google user for SpreadsheetService object");
			
			Properties properties = InstagramUtils.getConfigProperties();
			
			service.setUserCredentials(CryptoUtil.decrypt(properties.getProperty(Constants.USERNAME)),CryptoUtil.decrypt(properties.getProperty(Constants.PASSWORD)));
			URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
			
			SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
			this.spreadsheets  = feed.getEntries();
			System.out.println("bhavesh spreadsheets = "+spreadsheets);
			System.out.println("Fetching all google spreadsheets finished "+this.spreadsheets);
			for(SpreadsheetEntry tempSpreadsheet : this.spreadsheets )
		    {
		    	System.out.println("sheet name = "+tempSpreadsheet.getTitle().getPlainText());
		    }
			System.out.println("initialization of SheetHanlder ends \n");
		}
		catch(AuthenticationException ae)
		{
			ae.printStackTrace();
		}
		catch(MalformedURLException mue)
		{
			mue.printStackTrace();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch(ServiceException se)
		{
			se.printStackTrace();
		}
	}
	
	private SpreadsheetEntry getSpreadSheet(String spreadsheetName)
	{
		SpreadsheetEntry spreadsheet = null;
		System.out.println("selecting Excel "+spreadsheetName);
		for(SpreadsheetEntry tempSpreadsheet : this.spreadsheets )
	    {
	    	if(tempSpreadsheet.getTitle().getPlainText().equalsIgnoreCase(spreadsheetName))
	    	{
	    		spreadsheet = tempSpreadsheet;
	    		System.out.println("sheet name = "+tempSpreadsheet.getTitle().getPlainText());
	    		break;
	    	}	
	    }
	    return spreadsheet;
	}
	
	public List<String> getWorkSheetNameList(String spreadsheetName)
	{
		System.out.println("\n\ngetWorkSheetNameList action starts");
		ArrayList<String> listWorkSheetNames = new ArrayList<String>();
		SpreadsheetEntry spreadsheet = getSpreadSheet(spreadsheetName);
		WorksheetFeed worksheetFeed;
		
		try 
		{
			if(spreadsheet != null)
			{	
				worksheetFeed = this.service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
				List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
			
				for(WorksheetEntry tempWorksheet:worksheets)
				{
					listWorkSheetNames.add(tempWorksheet.getTitle().getPlainText());
					System.out.println("tempWorksheet.getTitle().getPlainText() ="+tempWorksheet.getTitle().getPlainText());
				}
			}
		}
		catch (IOException ioe)
		{
			// TODO Auto-generated catch block
			
		}
		catch (ServiceException se)
		{
			// TODO Auto-generated catch block
			
		}
		System.out.println("getWorkSheetNameList action ends");
		return listWorkSheetNames;
	}
	
	private WorksheetEntry selectWorkSheet(String spreadsheetName, String worksheetname)
	{
		SpreadsheetEntry spreadsheet = getSpreadSheet(spreadsheetName);
		WorksheetFeed worksheetFeed;
		WorksheetEntry worksheet = null;
		
		try 
		{
			if(spreadsheet != null)
			{	
				worksheetFeed = this.service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
				List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
				if(worksheetname.equalsIgnoreCase(""))
				{	
					worksheet = worksheets.get(0);			
				}
				else
				{	
					for(WorksheetEntry tempWorksheet:worksheets)
					{
						if(tempWorksheet.getTitle().getPlainText().equalsIgnoreCase(worksheetname))
						{
							worksheet = tempWorksheet;
						}	
					}
				}
			}
		}
		catch (IOException ioe)
		{
			// TODO Auto-generated catch block
			
		}
		catch (ServiceException se)
		{
			// TODO Auto-generated catch block
			
		}
		return worksheet;
	} 
	public String insertExcelRowRecent(String spreadsheetName, String worksheetName, List<LinkedHashMap<String, String>> listMapRowData)
	{
		String statusText = "";
		System.out.println("\n\ninsertExcelRow action starts for "+worksheetName);
		WorksheetEntry worksheet;
		
		worksheet = selectWorkSheet(Utils.checkNullValueForString(spreadsheetName),Utils.checkNullValueForString(worksheetName));
		if(worksheet != null)
		{	
		    URL listFeedUrl = worksheet.getListFeedUrl();
		    try 
		    {
				for(int i=listMapRowData.size()-1; i>=0;i--)
				{
			    	boolean rowFound = false;
					ListFeed listFeed = this.service.getFeed(listFeedUrl, ListFeed.class);
					int feedNo = listFeed.getTotalResults();
					int dayFeedNo = 1;
					if(listFeed.getTotalResults()>0)
					{
						ListEntry tempRow = listFeed.getEntries().get(listFeed.getTotalResults()-1);
						if(Utils.parseLong(listMapRowData.get(i).get("PostTime")) <= Utils.parseLong(tempRow.getCustomElements().getValue("PostTime")))
						{
							rowFound = true;
						}
						if(Utils.checkNullValueForString(listMapRowData.get(i).get("DayOfPost")).equalsIgnoreCase(Utils.checkNullValueForString(tempRow.getCustomElements().getValue("DayOfPost"))))
						{
							dayFeedNo = Utils.parseInt(tempRow.getCustomElements().getValue("PostsToday"))+1;
						}
					}
					if(!rowFound)
					{
						System.out.println("No duplicate row is found so going ahead with insertion");
						ListEntry row = new ListEntry();
						
						for(Map.Entry<String, String> entry : listMapRowData.get(i).entrySet()) 
						{
						    row.getCustomElements().setValueLocal(Utils.getPlainString(entry.getKey()), Utils.checkNullValueForString(entry.getValue()));
						}
						row.getCustomElements().setValueLocal("AuthorNumberofPosts",(feedNo+1)+"");
						row.getCustomElements().setValueLocal("PostsToday",dayFeedNo+"");
						row = service.insert(listFeedUrl, row);
						System.out.println("insertion finished");
					}
					else
					{
						System.out.println("Duplicate row is present");
						statusText = "Duplicate Row Found";
					}	
		    	}
			}
		    catch (IOException ioe)
		    {
				// TODO Auto-generated catch block
		    	statusText = "IOException";
			}
		    catch (ServiceException se)
		    {
				// TODO Auto-generated catch block
		    	statusText = "ServiceException";
			}
		}
		else
		{
			statusText = "Requested Excel not Available";
		}
		return statusText;
	}
	public void insertExcelRowAll(String spreadsheetName, String worksheetName, List<LinkedHashMap<String, String>> listMapRowData)
	{
		System.out.println("\n\ninsertExcelRow action starts for "+worksheetName);
		WorksheetEntry worksheet;
		
		worksheet = selectWorkSheet(Utils.checkNullValueForString(spreadsheetName),Utils.checkNullValueForString(worksheetName));
		if(worksheet != null)
		{	
		    URL listFeedUrl = worksheet.getListFeedUrl();
		    try 
		    {
				for(int i=listMapRowData.size()-1; i>=0;i--)
				{
					ListFeed listFeed = this.service.getFeed(listFeedUrl, ListFeed.class);
					int feedNo = listFeed.getTotalResults();
					int dayFeedNo = 1;
					if(listFeed.getTotalResults()>0)
					{
						ListEntry tempRow = listFeed.getEntries().get(listFeed.getTotalResults()-1);
						if(Utils.checkNullValueForString(listMapRowData.get(i).get("DayOfPost")).equalsIgnoreCase(Utils.checkNullValueForString(tempRow.getCustomElements().getValue("DayOfPost"))))
						{
							dayFeedNo = Utils.parseInt(tempRow.getCustomElements().getValue("PostsToday"))+1;
						}
					}
					ListEntry row = new ListEntry();
					
					for(Map.Entry<String, String> entry : listMapRowData.get(i).entrySet()) 
					{
					    row.getCustomElements().setValueLocal(Utils.getPlainString(entry.getKey()), Utils.checkNullValueForString(entry.getValue()));
					}
					row.getCustomElements().setValueLocal("AuthorNumberofPosts",(feedNo+1)+"");
					row.getCustomElements().setValueLocal("PostsToday",dayFeedNo+"");
					row = service.insert(listFeedUrl, row);
					System.out.println("insertion finished");
		    	}
			}
		    catch (IOException ioe)
		    {
				// TODO Auto-generated catch block
			}
		    catch (ServiceException se)
		    {
				// TODO Auto-generated catch block
			}
		}
		else
		{
		}
	}
	public int getNoOfRows(String spreadsheetName, String worksheetName)
	{
		System.out.println("\n\ngetNoOfRows action starts");
		int feedNo = 0;
		WorksheetEntry worksheet;
		
		worksheet = selectWorkSheet(Utils.checkNullValueForString(spreadsheetName),Utils.checkNullValueForString(worksheetName));
		if(worksheet != null)
		{	
		    URL listFeedUrl = worksheet.getListFeedUrl();
		    try 
		    {
				ListFeed listFeed = this.service.getFeed(listFeedUrl, ListFeed.class);
				feedNo = listFeed.getTotalResults();
		    }
		    catch (IOException ioe)
		    {
				// TODO Auto-generated catch block
		    	ioe.printStackTrace();
			}
		    catch (ServiceException se)
		    {
				// TODO Auto-generated catch block
		    	se.printStackTrace();
			}
		}
		return feedNo;
	}
	public long getLastRowPostTime(String spreadsheetName, String worksheetName)
	{
		System.out.println("\n\ngetLastRowPostTime action starts");
		long lastPostTime = 0L;
		WorksheetEntry worksheet;
		
		worksheet = selectWorkSheet(Utils.checkNullValueForString(spreadsheetName),Utils.checkNullValueForString(worksheetName));
		if(worksheet != null)
		{	
		    URL listFeedUrl = worksheet.getListFeedUrl();
		    try 
		    {
		    	ListFeed listFeed = this.service.getFeed(listFeedUrl, ListFeed.class);
		    	int totalRows = listFeed.getTotalResults();
		    	if(totalRows>0)
				{
					ListEntry tempRow = listFeed.getEntries().get(totalRows-1);
					lastPostTime = Utils.parseLong(tempRow.getCustomElements().getValue("PostTime"));
				}		
		    }
		    catch (IOException ioe)
		    {
				// TODO Auto-generated catch block
		    	ioe.printStackTrace();
			}
		    catch (ServiceException se)
		    {
				// TODO Auto-generated catch block
		    	se.printStackTrace();
			}
		}
		return lastPostTime;
	}
	
	public String renameSheetToBusy(String spreadsheetName, String worksheetName)
	{
		System.out.println("\n\nrenameSheetToBusy action starts");
		WorksheetEntry worksheet;
		String newWorkSheetName = worksheetName;
		worksheet = selectWorkSheet(Utils.checkNullValueForString(spreadsheetName),Utils.checkNullValueForString(worksheetName));
		if(worksheet != null)
		{	
		    worksheet.setTitle(new PlainTextConstruct(worksheetName+"*"));
		    System.out.println("changed sheet name = "+worksheet.getTitle().getPlainText());
		    try 
		    {
				worksheet.update();
				newWorkSheetName = worksheetName+"*";
			}
		    catch (IOException e) 
		    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    catch (ServiceException e) 
		    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}  
		return newWorkSheetName;
	}
	public void renameSheetRemoveBusy(String spreadsheetName, String worksheetName)
	{
		System.out.println("\n\nrenameSheetRemoveBusy action starts");
		WorksheetEntry worksheet;
		worksheet = selectWorkSheet(Utils.checkNullValueForString(spreadsheetName),Utils.checkNullValueForString(worksheetName));
		worksheetName = Utils.checkNullValueForString(worksheetName.replace('*', ' '));
		System.out.println("worksheetName = "+worksheetName);
		if(worksheet != null)
		{	
		    worksheet.setTitle(new PlainTextConstruct(worksheetName));
		    try 
		    {
				worksheet.update();
			}
		    catch (IOException e) 
		    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    catch (ServiceException e) 
		    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
