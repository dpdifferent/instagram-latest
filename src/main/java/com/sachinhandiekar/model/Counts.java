package com.sachinhandiekar.model;


public class Counts {
	
	private int follows;

	private int followedBy;

	private int media;
	
	private int postsToday;
	
	private int postLikes;
	
	public int getMedia() {
		return media;
	}

	public void setMedia(int media) {
		this.media = media;
	}

	public int getFollows() {
		return follows;
	}

	public void setFollows(int follows) {
		this.follows = follows;
	}

	public int getFollowedBy() {
		return followedBy;
	}

	public void setFollowedBy(int followedBy) {
		this.followedBy = followedBy;
	}

	public int getPostsToday() {
		return postsToday;
	}

	public void setPostsToday(int postsToday) {
		this.postsToday = postsToday;
	}

	public int getPostLikes() {
		return postLikes;
	}

	public void setPostLikes(int postLikes) {
		this.postLikes = postLikes;
	}


}
