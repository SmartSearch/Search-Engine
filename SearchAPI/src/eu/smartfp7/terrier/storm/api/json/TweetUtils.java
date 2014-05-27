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
import java.util.Calendar;
import java.util.Date;

public class TweetUtils {
	public static String getRelativeTime(Tweet t){
		StringBuffer sb= new StringBuffer();
		
		try{
			
			DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
			Date d = df.parse(t.getDate());
			Calendar tweetcal  = Calendar.getInstance();
			tweetcal.setTime(d);
			
			
			long diffInMillis = Calendar.getInstance().getTimeInMillis()-tweetcal.getTimeInMillis();
			
			long diffInSec = diffInMillis/1000;
			
			
			long diffInMin = diffInSec/60;
			
			if(diffInSec<0 || diffInMin==0){				
				sb.append("Now");
			}					
			else if(diffInMin<60)
			{
				sb.append(diffInMin+"m");
			}else{
				long diffInHrs = diffInMin/60;
				sb.append(diffInHrs+"h");
			}
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
		
	}
	
	public static String getShortCleanDate(Tweet t){
		StringBuilder sb = new StringBuilder();
		try{
			DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
			Date d = df.parse(t.getDate());
			
			df = new SimpleDateFormat("HH:mm - dd MMM yy");
			
			sb.append(df.format(d));
		}catch(Exception e){
			
		}
		
		return sb.toString();
	}
	
	
			
}
