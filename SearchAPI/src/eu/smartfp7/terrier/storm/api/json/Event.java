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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import eu.smartfp7.terrier.storm.api.conf.SmartProperties;

public class Event extends Result implements Serializable{
	
	
	private int id;
	private String startTime;
	private String activity;
	private int locationId;
	private String locationName;
	private double density;
	
	static List<Location> locations;
	
	static{
		locations = new ArrayList<Location>();
		String edgeNodes = SmartProperties.getInstance().getProperty(SmartProperties.EDGE_NODES);
		
		String[] edgeDescriptions = edgeNodes.split(";");		
		for(int i=0;i<edgeDescriptions.length;i++){				
			String d = edgeDescriptions[i];
			String comps[]= d.split(",");
			Location l = new Location((float)Double.parseDouble(comps[2]),(float)Double.parseDouble(comps[1]),comps[0] );
			locations.add(l);
		}
	}
	
	
	
	public Event() {
		super();
	}
	public Event(int id, String startTime, String activity, int locationId) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.activity = activity;
		this.locationId = locationId;
		if(this.locationId<locations.size()) this.locationName = locations.get(locationId).getName();
	}
	
	public Event(int id, String startTime, String activity, String density, int locationId) {
		this(id,startTime,activity,locationId);
		try{
			this.density = Double.parseDouble(density);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public double getDensity() {
		return density;
	}
	public void setDensity(double density) {
		this.density = density;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
	@Override
	public  boolean isBefore( Calendar beforeCal){
		
		try{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = df.parse(this.getStartTime());
			
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			
			return (c.getTimeInMillis()<=beforeCal.getTimeInMillis());		
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
		
	}
	@Override
	public boolean inLocation(int locationId) {
		
		return this.locationId == locationId;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
		
	
	
	
	

}
