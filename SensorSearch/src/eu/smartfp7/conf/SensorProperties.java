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


package eu.smartfp7.conf;




import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;


public class SensorProperties {
	Properties properties= new Properties();
	
	static SensorProperties one;
	
	public final static String EDGE_NODES="edgenodes";	
	
	
	private SensorProperties(){		
		InputStream is;
		try {
			//URL url =  ClassLoader.getSystemResource("sensor.properties");
			is = this.getClass().getResourceAsStream("/sensor.properties");
			properties.load(is);
			
		}catch (IOException e) {
			System.err.println("Error in reading the application properties file");
			
		}
//		catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			System.err.println("Error in reading the application properties file");
//		}		
	}
	
	
	public static SensorProperties getInstance(){
		if(one==null)
			one = new SensorProperties();
		return one;			
	}
	
	
	public String getProperty(String key){
		return properties.getProperty(key);		
	}

}
