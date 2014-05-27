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

public class Observation implements Serializable{
	String dateStr;
	
	double density;
	
	double[] colors;
	
	String activity;

	public Observation(String dateStr, double density, double[] colors, String activity) {
		super();
		this.dateStr = dateStr;
		this.density = density;
		this.colors = colors;
		this.activity = activity;
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
		return activity;
	}
	
	
	
}