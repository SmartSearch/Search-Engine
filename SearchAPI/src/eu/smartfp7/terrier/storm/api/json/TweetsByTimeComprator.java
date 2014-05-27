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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class TweetsByTimeComprator implements Comparator<Tweet> {

	@Override
	public int compare(Tweet o1, Tweet o2) {
		// We expect the dates in the tweet to be in this format Thu Jun 14 14:05:49 +0000 2012
		if(o1.getScore()==o2.getScore())		
			try{			
				String s1 = o1.getDate();
				String s2 = o2.getDate();			
				DateFormat df = new SimpleDateFormat("EEE MMM dd hh:mm:ss Z yyyy");			
				return df.parse(s2).compareTo(df.parse(s1));
			}catch(Exception e){
				return 0;
			}
		else return new Double(o2.getScore()).compareTo(o1.getScore());
		
	}
	
	}
