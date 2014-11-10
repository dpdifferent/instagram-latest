package com.sachinhandiekar.examples;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.comments.CommentData;
import org.jinstagram.entity.comments.MediaCommentsFeed;
import org.jinstagram.entity.common.Pagination;
import org.jinstagram.entity.media.MediaInfoFeed;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.jinstagram.exceptions.InstagramException;

import com.dmt.utility.Utils;
import com.sachinhandiekar.model.AppUser;

public class InstagramHandler 
{

	private AppUser user;
	private int userId = 0;
    Instagram instagram = null;
    UserInfo userInfo = null;
	MediaFeed mediaFeed = null;
    
	public InstagramHandler()
	{
	}
	
	public void initialize()
	{

	}

	public List<LinkedHashMap<String, String>> getUserRecentUpdates(Instagram instagram, String userName, Long lastPostTime)
	{
		UserFeed userFeed = null;
		try
		{
			userFeed = instagram.searchUser(userName);
		} 
		catch (InstagramException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       	List<UserFeedData> userList = userFeed.getUserList();
    	long userId = userList.get(0).getId();
		List<LinkedHashMap<String, String>>  listMapRowData= new ArrayList<LinkedHashMap<String, String>>();
		try
		{
			userInfo = instagram.getUserInfo(userId);
			mediaFeed = instagram.getRecentMediaFeed(userId);
		}
		catch (InstagramException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		putUserFeedData(instagram, mediaFeed, listMapRowData, lastPostTime);
 		return listMapRowData;
	}	
	
	private void putUserFeedData(Instagram instagram, MediaFeed mediaFeed, List<LinkedHashMap<String, String>>  listMapRowData, Long lastPostTime)
	{
		//System.out.println("putting feed data on map");
		if(mediaFeed != null)
		{
			TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
			
			DateFormat formatter = new SimpleDateFormat("MMMM d, yyyy");
 		    DateFormat format = new SimpleDateFormat("HH:mm:ss a z");
 		    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
 		    formatter.setTimeZone(timezone);
 		    format.setTimeZone(timezone);
 		    
			for(MediaFeedData feedData : mediaFeed.getData())
			{
				Calendar c = Calendar.getInstance();
				String createdDate = feedData.getCreatedTime();
	 			if(Utils.parseLong(createdDate)<= lastPostTime)
	 			{
	 				continue;
	 			}	
	 		    Date lastDate = new Date(Long.parseLong(createdDate)*1000L);
	 		   
	 		    c.setTime(lastDate);
	 		    //c.add(Calendar.DATE ,1);
	 		    int postLikes = 0;
	 		    int postComments = 0;
	 		    int authorFollowers = userInfo.getData().getCounts().getFollwed_by();
			    if(feedData.getLikes() != null)
			    {
			    	postLikes = feedData.getLikes().getCount();
			    }
			    if(feedData.getComments() != null)
			    {
			    	postComments = feedData.getComments().getCount();
			    }
			    
	 		    double engpct = 0.0d;
	 		    if(authorFollowers != 0)
	 		    {
	 		    	engpct = ((double)(postLikes+postComments)/authorFollowers)*100.00;
	 		    }
	 		    String dateDay = formatter.format(lastDate);
	 		    String dateTime = format.format(lastDate);
	 		    String dayOfWeek = dayFormat.format(lastDate);
	 		    //String nextDate = formatter.format(c.getTime());
	
	 		    LinkedHashMap<String, String> mapRowData = new LinkedHashMap<String, String>(); 
	 		    mapRowData.put("AuthorUsername", userInfo.getData().getUsername());
			    mapRowData.put("AuthorFollowingAtUsers", userInfo.getData().getCounts().getFollows()+"");
			    mapRowData.put("AuthorFollowers", authorFollowers+"");
			    mapRowData.put("PostURL", feedData.getLink()); 
				if(feedData.getLocation() != null)
				{	
					mapRowData.put("PostLocationCountry", feedData.getLocation().getName());
					mapRowData.put("Post Location Co ordinates", feedData.getLocation().getLatitude() + ", "+feedData.getLocation().getLongitude());
				}	
				
	 		    mapRowData.put("DayOfPost", dateDay);
	 		    mapRowData.put("Eng.Pct.", engpct+"%");
			    mapRowData.put("Day", dayOfWeek);
			    mapRowData.put("Timeofpost", dateTime);
			    mapRowData.put("PostTime", createdDate);
			    if(feedData.getCaption() != null)
			    {
			    	mapRowData.put("Caption", feedData.getCaption().getText());
			    	String usernames = getUsernameFromCaption(feedData.getCaption().getText());
			    	mapRowData.put("Shoutouts", usernames);
			    }
			    	mapRowData.put("Postlikes", postLikes+"");
	 		    
	 		    String tags = "";
	 		    int count = 0;
	 		    for(String tag: feedData.getTags())
	 		    {
	 		    	tags=tags+"#"+tag;
	 		    	count++;
	 		    }
	 		   mapRowData.put("Tags", tags); 
	 		   mapRowData.put("NumberofTags", count+"");
	 		   if(feedData.getComments() != null)
	 		   {	  
	 			   mapRowData.put("PostComments",  postComments + "");
	 			   System.out.println("this media post is not present on Spreadsheet therefore making a call to get all commenters");
	 			   String commenterList = getAllCommentersList(instagram, feedData.getId());
	 			   mapRowData.put("PostCommenters", commenterList);
	 		   } 
	 		   listMapRowData.add(mapRowData);	
			}	
		}
	}
	
	public List<LinkedHashMap<String, String>> getUserAllPastData(Instagram instagram, String userName)
	{
		System.out.println("getUserAllPastData called");
		UserFeed userFeed = null;
		List<LinkedHashMap<String, String>>  listMapRowData = new ArrayList<LinkedHashMap<String, String>>();
		try
		{
			userFeed = instagram.searchUser(userName);
		} 
		catch (InstagramException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       	List<UserFeedData> userList = userFeed.getUserList();
    	long userId = userList.get(0).getId();
    	try
		{
			userInfo = instagram.getUserInfo(userId);
		}
		catch (InstagramException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	try 
    	{
    		int count = instagram.getUserInfo(userId).getData().getCounts().getMedia();
    		String maxFeedId = instagram.getRecentMediaFeed(userId).getData().get(0).getId();
			mediaFeed = instagram.getRecentMediaFeed(userId, count, "", maxFeedId, null, null);
			System.out.println("Feed Size = "+mediaFeed.getData().size());    
			putUserFeedData(instagram, mediaFeed, listMapRowData,0L); 

	 		getUserDataNextPage(instagram, mediaFeed.getPagination(), listMapRowData);
			
		} 
    	catch (InstagramException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return listMapRowData;
	}

	public void getUserDataNextPage(Instagram instagram,Pagination pagination, List<LinkedHashMap<String, String>>  listMapRowData)
	{
		try
		{
			while(pagination.getNextUrl() != null)
			{
				mediaFeed = instagram.getRecentMediaNextPage(pagination);
				System.out.println("Feed Size = "+mediaFeed.getData().size());
				putUserFeedData(instagram, mediaFeed, listMapRowData,0L);
				pagination = mediaFeed.getPagination();
			}
		} 
		catch (InstagramException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		//return mediaFeed.getPagination();
	}
	
	 private String getAllCommentersList(Instagram instagram, String mediaId)
	 {
	  String commenterList = "";
	  HashSet<String> hashsetCommenters = new HashSet<String>();
	  
	  try 
	  {
	   MediaCommentsFeed mediaCommentsFeed = instagram.getMediaComments(mediaId);
	   String comma = "";
	   String userId="";
	   for(CommentData cdata : mediaCommentsFeed.getCommentDataList())
	   {
	    if(cdata.getCommentFrom() != null)
	    {
	     userId = cdata.getCommentFrom().getUsername();
	     hashsetCommenters.add(userId);
	    }
	   }
	   for(String id: hashsetCommenters)
	   {
	    commenterList = commenterList + comma + id;
	    comma = ", ";
	   }
	  }
	  catch (InstagramException e) 
	  {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  
	  return commenterList;
	 }
	
	private String getUsernameFromCaption(String caption)
	{
		String username = "";
		String comma = "";
		ArrayList<String> listAllWords = new ArrayList<String>(Arrays.asList(caption.split(" ")));
		ArrayList<String> listDesiredWords = new ArrayList<String>();
		
		for(String word : listAllWords)
		{
			if(word.startsWith("@"))
			{
				if(word.indexOf("@", 1)>0)
				{
					ArrayList<String> listAll = new ArrayList<String>(Arrays.asList(word.split("@")));
					int count = 0;
					for(String w :listAll)
					{
						if(count == 0)
						{	
							count++;
							continue;
						}	
						listDesiredWords.add("@" + w);
						count++;
					}
				}
				else
				{	
					listDesiredWords.add(word);
				}
			}
		}
		for(String word: listDesiredWords)
		{
			username = username + comma + word;
			comma = ", ";
		}	
		return username;
	}
		
	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Instagram getInstagram() {
		return instagram;
	}

	public void setInstagram(Instagram instagram) {
		this.instagram = instagram;
	}
	
}
