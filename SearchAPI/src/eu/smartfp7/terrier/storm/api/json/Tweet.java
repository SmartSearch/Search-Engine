/*  
 * SMART FP7 - Search engine for MultimediA enviRonment generated contenT
 * Webpage: http://smartfp7.eu
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 * 
 * The Original Code is Copyright (c) 2012-2013 the University of Glasgow
 * All Rights Reserved
 * 
 * Contributor(s):
 *  Dyaa Albakour <dyaa.albakour@glasgow.ac.uk>
 */


package eu.smartfp7.terrier.storm.api.json;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import eu.smartfp7.terrier.storm.api.conf.SmartProperties;


public class Tweet extends Result implements  Serializable {
	
	static List<Location> areas = new ArrayList<Location>();
	
	
	static{		
		String areasProp = SmartProperties.getInstance().getProperty(SmartProperties.AREAS);
		String[] allAreas = areasProp.split(";");
		for(String a:allAreas){			 
			String[] comps = a.split(",");
			float lon1 = (float)Double.parseDouble(comps[1]);
			float lat1 = (float)Double.parseDouble(comps[2]);
			float lon2 = (float)Double.parseDouble(comps[3]);
			float lat2 = (float)Double.parseDouble(comps[4]);
			
			float lat = (lat1+lat2)/2.0f;
			float lon = (lon1+lon2)/2.0f;
			
			Location area = new Location(lat,lon,comps[0]);
			areas.add(area);			
		}		
	}
	
	
	
	private static final long serialVersionUID = 1L;
	private String rank;
	private String date;
	private String username; // user.screen_name
	private String userDisplay; // user.name
	

	private String imageurl;
	private String tweet;
	private int docid;
	private int shardid;
	
	private String urls;


	private String userMentions;
	private String hashtags;
	
	private String relativeTime;
	private String niceShortDate;
	
	private boolean mappable;
	
	private double lon, lat;
	
	private int areaId;
	private String areaName;



	public Tweet() {}

	public Tweet(int docid, double score, String rank, String date, String username, String imageURL, String tweet, int shardid) {
		this.rank = rank;
		this.docid = docid;
		this.score = score;
		this.date = date;
		this.username = username;
		this.imageurl = imageURL;
		this.tweet = tweet;
		this.shardid = shardid;
		
		this.description = tweet;
		this.title = username;
		
		this.areaId = shardid;
		if(this.areaId-1<areas.size()) this.areaName = areas.get(areaId-1).getName();
		
	}
	
	
	

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public int getShardid() {
		return shardid;
	}

	public void setShardid(int shardid) {
		this.shardid = shardid;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getDocid() {
		return docid;
	}

	public void setDocid(int docid) {
		this.docid = docid;
	}

	public String getRank() {
		return rank;
	}

	public String getDate() {
		return date;
	}

	public String getUsername() {
		return username;
	}

	public String getImageurl() {
		return imageurl;
	}

	public String getTweet() {
		return tweet;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
	
	
	
	public String getUserMentions() {
		return userMentions;
	}

	public void setUserMentions(String userMentions) {
		this.userMentions = userMentions;
	}

	public String getHashtags() {
		return hashtags;
	}

	public void setHashtags(String hashtags) {
		this.hashtags = hashtags;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}
	
	public String getRelativeTime() {
		return relativeTime;
	}

	public void setRelativeTime(String relativeTime) {
		this.relativeTime = relativeTime;
	}

	public String getNiceShortDate() {
		return niceShortDate;
	}

	public void setNiceShortDate(String niceShortDate) {
		this.niceShortDate = niceShortDate;
	}	

	public String getUserDisplay() {
		return userDisplay;
	}

	public void setUserDisplay(String userDisplay) {
		this.userDisplay = userDisplay;
	}	
	
	public boolean isMappable() {
		return mappable;
	}

	public void setMappable(boolean mappable) {
		this.mappable = mappable;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
	
	
	public Tweet(String JSONString){
		
		
		
		//this.docid = (int) Long.parseLong(obj.get("id").isString().stringValue());
//		this.hashtags= obj.get("hashtagEntities").isArray().toString();
	}
	@Override
	public  boolean isBefore( Calendar beforeCal){
		
		try{
			DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
			Date d = df.parse(this.getDate());
			
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			
			return (c.getTimeInMillis()<=beforeCal.getTimeInMillis());		
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
		
	}

	@Override
	public boolean inLocation(int locationId) {
		
		return this.areaId == locationId;
	}
	
	
}
