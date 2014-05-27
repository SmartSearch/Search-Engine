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

import eu.smartfp7.terrier.client.RSSStormTCPAPI;
import eu.smartfp7.terrier.client.SensorStormTCPAPI;
import eu.smartfp7.terrier.client.StormAPI;
import eu.smartfp7.terrier.client.TweetsStormTCPAPI;
import eu.smartfp7.terrier.storm.api.conf.SmartProperties;

/**
 * This class is written for the Prisa Demo.
 * 
 * It is a Singleton Entry for the APIs for searching both the tweets and the Sensor stuff.
 * It is designed to replace the APIUtil class.
 * 
 * @author Dyaa Albakour
 *
 */
public class APIsWrapper {
	private static APIsWrapper one;
	
	private SensorStormTCPAPI sensorapi;

	private TweetsStormTCPAPI tweetsapi;
	
	private RSSStormTCPAPI rssapi;
	
	private  APIsWrapper() {
		//StormAPI rtr = new TerrierAPIj();
		 SmartProperties props = SmartProperties.getInstance();
		 
		 String api_conf = props.getProperty(SmartProperties.SMART_API);
		 
		 String host = props.getProperty(SmartProperties.SMART_SENSORHOST);
		 int port = Integer.parseInt(props.getProperty(SmartProperties.SMART_SENSORPORT));
		 		 
		 this.sensorapi = new SensorStormTCPAPI(host,port);
		 this.sensorapi.init(); 
		 
		 host = props.getProperty(SmartProperties.SMART_TWEETHOST);
		 port = Integer.parseInt(props.getProperty(SmartProperties.SMART_TWEETPORT));
		 
		 
		 this.tweetsapi = new TweetsStormTCPAPI(host,port);
		 this.tweetsapi.init();		
		 
		 host = props.getProperty(SmartProperties.SMART_RSSHOST);
		 port = Integer.parseInt(props.getProperty(SmartProperties.SMART_RSSPORT));
		 
		 this.rssapi = new RSSStormTCPAPI(host, port);
		 this.rssapi.init();
		 
		 
	}
	
	public static APIsWrapper getInstance(){		
		if(one==null)
			one = new APIsWrapper();		
		return one;
		
	}
	
	public RSSStormTCPAPI getRSSStormAPI(){
		System.err.println("Returning an api for " + Thread.currentThread());
		return rssapi;
	}
	
	public SensorStormTCPAPI getSensorStormAPI(){
		System.err.println("Returning an api for " + Thread.currentThread());
		return sensorapi;
	}
	
	public TweetsStormTCPAPI getTweetsStormAPI(){
		System.err.println("Returning an api for " + Thread.currentThread());
		return tweetsapi;
	}
	

}
