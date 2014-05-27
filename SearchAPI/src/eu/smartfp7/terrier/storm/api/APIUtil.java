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


package eu.smartfp7.terrier.storm.api;


import eu.smartfp7.terrier.client.SensorStormTCPAPI;
import eu.smartfp7.terrier.client.StormAPI;
import eu.smartfp7.terrier.client.TerrierAPIj;
import eu.smartfp7.terrier.client.TweetsStormTCPAPI;
import eu.smartfp7.terrier.storm.api.conf.SmartProperties;

public class APIUtil {
	/*
	final static ThreadLocal<StormAPI> api = new ThreadLocal<StormAPI>()
	{
		 @Override protected StormAPI initialValue() {
			 //StormAPI rtr = new TerrierAPIj();
			 SmartProperties props = SmartProperties.getInstance();
			 String host = props.getProperty(SmartProperties.SMART_QUERYHOST);
			 int port = Integer.parseInt(props.getProperty(SmartProperties.SMART_QUERYPORT));
			 
			 
			 //StormAPI rtr = new SensorStormTCPAPI(host,port);
			 StormAPI rtr = new TweetsStormTCPAPI(host,port);
			 rtr.init();
			 return rtr;
		 }
	};*/
	
	/*		
	public static StormAPI getAPI()
	{
		System.err.println("Returning an api for " + Thread.currentThread());
		return api.get();
	}*/
	
	private static APIUtil one;
	
	private StormAPI api;
	
	private APIUtil(){
		//StormAPI rtr = new TerrierAPIj();
		 SmartProperties props = SmartProperties.getInstance();
		 String host = props.getProperty(SmartProperties.SMART_QUERYHOST);
		 int port = Integer.parseInt(props.getProperty(SmartProperties.SMART_QUERYPORT));
		 
		 
		 String api_conf = props.getProperty(SmartProperties.SMART_API);
		 StormAPI rtr;
		 if(api_conf.equals("sensor"))		 
			 rtr = new SensorStormTCPAPI(host,port);
		 else 
			 rtr = new TweetsStormTCPAPI(host,port);
		 rtr.init();
		 api = rtr;
	}
	
	public static APIUtil getInstance(){
		
		if(one==null)
			one = new APIUtil();		
		return one;
	}
	
	public StormAPI getAPI()
	{
		System.err.println("Returning an api for " + Thread.currentThread());
		return api;
	}
	
}
