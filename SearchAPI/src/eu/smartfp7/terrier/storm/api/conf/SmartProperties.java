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


package eu.smartfp7.terrier.storm.api.conf;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;


public class SmartProperties {
	Properties properties= new Properties();
	
	static SmartProperties one;
	
	
	public final static String SMART_API="api";
	
	public final static String SMART_PORT="port";
	
	public final static String SMART_QUERYHOST="queryhost";	
	public final static String SMART_QUERYPORT = "queryport";
	
	public final static String SMART_TWEETHOST="tweethost";	
	public final static String SMART_TWEETPORT = "tweetport";
	
	public final static String SMART_SENSORHOST="sensorhost";	
	public final static String SMART_SENSORPORT = "sensorport";
	
	
	public final static String SMART_LATESTHOST="latesthost";
	public final static String SMART_LATESTPORT="latestport";
	

	public final static String SMART_RSSHOST="rsshost";
	public final static String SMART_RSSPORT="rssport";
	
	
	public final static String EDGE_NODES="edgenodes";
	public final static String AREAS="areas";
	
	
	
	
	private SmartProperties(){		
		FileInputStream is;
		try {
			URL url =  ClassLoader.getSystemResource("smart.properties");
			is = new FileInputStream(new File(url.toURI()));
			properties.load(is);
			
		}catch (IOException e) {
			System.err.println("Error in reading the application properties file");
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.err.println("Error in reading the application properties file");
		}		
	}
	
	
	public static SmartProperties getInstance(){
		if(one==null)
			one = new SmartProperties();
		return one;			
	}
	
	
	public String getProperty(String key){
		return properties.getProperty(key);		
	}

}
