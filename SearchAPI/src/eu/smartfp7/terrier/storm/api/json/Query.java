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

import java.util.HashMap;
import java.util.Map;

public class Query {

	String type = "search";
	String query;
	Map<String,String> params = new HashMap<String,String>();

	String trending;
	
	
	public Query(String q) {
		this.query = q;
	}
	
	
	
	
	public String getTrending() {
		return trending;
	}




	public void setTrending(String trending) {
		this.trending = trending;
	}




	public void setParameter(String name, String value){
		this.params.put(name, value);
	}
	
	public String getParameter(String name){
		return this.params.get(name);
	}
}
