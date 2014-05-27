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

/**
 * This class represents nodes(edges) in the smart framework
 * 
 * @author dyaa
 *
 */
public class Node {
	
	
	private double lattiude;
	private double longitude;
	
	private String id;
	private String fullName;
	
	public double getLattiude() {
		return lattiude;
	}
	public void setLattiude(double lattiude) {
		this.lattiude = lattiude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Node(double lattiude, double longitude, String id, String fullName) {
		super();
		this.lattiude = lattiude;
		this.longitude = longitude;
		this.id = id;
		this.fullName = fullName;
	}
	public Node() {
		super();
	}
	
	
	

}
