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


package eu.smartfp7.terrier.sensor;

import java.util.HashMap;
import java.util.Map;

public class Observation{
	String dateStr;
	
	double density;
	
	double[] colors;
	
	Map<String,String> metaEntries;

	public Observation(String dateStr, double density, double[] colors, String activity) {
		super();
		this.dateStr = dateStr;
		this.density = density;
		this.colors = colors;
		this.metaEntries= new HashMap<String, String>();
		this.metaEntries.put("activity",activity);
	}
	
	
	public Observation(String dateStr, double density, double[] colors, Map<String, String> metaEntries) {
		super();
		this.dateStr = dateStr;
		this.density = density;
		this.colors = colors;
		this.metaEntries= metaEntries;
	}

	public Observation() {
		super();
	}

	public String getDateStr() {
		return dateStr;
	}


	public double getDensity() {
		return density;
	}

	public double[] getColors() {
		return colors;
	}

	public String getActivity() {
		if (metaEntries.containsKey("activity"))			
			return this.metaEntries.get("activity");
		else
			return "";
	}


	public Map<String, String> getMetaEntries() {
		return metaEntries;
	}
	
	
	
	
	
}