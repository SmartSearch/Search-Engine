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

import java.io.Serializable;



/**
 * 
 * A Location where the sensor data is stemming from.
 * The current implementation maps each location to an edge node. This generally should not be the case. 
 * 
 * @author dyaa albakour
 *
 */
public class Location implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2358543769864918295L;

	protected String name;
	
	protected double lon, lat;

	
	protected String host;
	protected String dbname;
	
		/**
	 * A unique identifier of a location
	 */
	protected String id;
	
	
	public Location(String id, String name, double lon, double lat) {
		super();
		this.id = id;
		this.name = name;
		this.lon = lon;
		this.lat = lat;
	}
	
	public Location(String id, String name, double lon, double lat, String host, String dbname) {
		super();
		this.id = id;
		this.name = name;
		this.lon = lon;
		this.lat = lat;
		this.dbname = dbname;
		this.host= host;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public double getLon() {
		return lon;
	}


	public double getLat() {
		return lat;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	
	
	

}