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


package eu.smartfp7.terrier.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;

import eu.smartfp7.terrier.storm.api.json.Tweet;
import eu.smartfp7.terrier.storm.api.json.TweetUtils;


public class TweetsStormTCPAPI extends StormTCPAPI {
	public interface TweetResultsCallback extends ResultsCallback<Tweet>
	{}

	public TweetsStormTCPAPI(String queryhost, int queryport) {
		super(queryhost, queryport);
	}

	protected Serializable unmarshall(DataInputStream datin, int rank)
			throws IOException {
		String srank = String.valueOf(rank+1);
		int docid = datin.readInt();
		double score = datin.readDouble();
		int shardid = datin.readInt();
		String date = datin.readUTF();
		String username = datin.readUTF();
		String imageurl = datin.readUTF();
		String tweet = datin.readUTF();							
		
		String hashtags=datin.readUTF();
		String urls = datin.readUTF();
		String userMentions = datin.readUTF();
		
		String authorDisplay = datin.readUTF();
		
		String geolat = datin.readUTF();
		String geolng = datin.readUTF();
		
		
		
		
		Tweet t = new Tweet(docid, score, srank,date,username,imageurl,tweet,shardid);
		t.setUserMentions(userMentions);
		t.setHashtags(hashtags);
		t.setUrls(urls);						
		t.setUserDisplay(authorDisplay);
		
		t.setRelativeTime(TweetUtils.getRelativeTime(t));
		t.setNiceShortDate(TweetUtils.getShortCleanDate(t));
		
		try{
			double lat = Double.parseDouble(geolat);
			double lng = Double.parseDouble(geolng);
			
			t.setMappable(true);
//								t.setLat(new Random().nextDouble()*60.0);
//								t.setLon(new Random().nextDouble()*120);
			t.setLat(lat);
			t.setLon(lng);
			
			System.err.println("Suceecfully parsed geo location info for pair\t"+geolat+"\t"+geolng);
		}catch(Exception e){
			System.err.println("Could not parse geo location info for pair\t"+geolat+"\t"+geolng);
		}
		return t;
	}


	
}
