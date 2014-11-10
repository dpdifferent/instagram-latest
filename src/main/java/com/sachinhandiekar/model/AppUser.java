package com.sachinhandiekar.model;

import java.util.List;


public class AppUser {
	
	private long userId;
	private String userName;
	private Counts counts = new Counts();
	private String dayOfPost;
	private String timeOfPost;
	private String postUrl;
	private String postLocation;
	private String postCaption;
	private List<String> postTags;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Counts getCounts() {
		return counts;
	}
	public void setCounts(Counts counts) {
		this.counts = counts;
	}
	
	public String getDayOfPost() {
		return dayOfPost;
	}
	public void setDayOfPost(String dayOfPost) {
		this.dayOfPost = dayOfPost;
	}
	
	public String getTimeOfPost() {
		return timeOfPost;
	}
	public void setTimeOfPost(String timeOfPost) {
		this.timeOfPost = timeOfPost;
	}
	
	public String getPostUrl() {
		return postUrl;
	}
	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}
	
	public String getPostLocation() {
		return postLocation;
	}
	public void setPostLocation(String postLocation) {
		this.postLocation = postLocation;
	}
	
	public String getPostCaption() {
		return postCaption;
	}
	public void setPostCaption(String postCaption) {
		this.postCaption = postCaption;
	}

	public List<String> getPostTags() {
		return postTags;
	}
	public void setPostTags(List<String> postTags) {
		this.postTags = postTags;
	}
	
}
