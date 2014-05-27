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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RSSFeed extends Result{

	
	String date;
	String permalink;
	int areaId;
	
	public RSSFeed(String title, String description, String link, String permalink, String dateStr, int areaId) {
		super();
		this.title = title;
		this.description = description;
		this.URI = link;
		
		this.date = dateStr;		
		this.permalink = permalink;
		this.areaId = areaId;
	}

	@Override
	public boolean isBefore(Calendar beforeCal) {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d=null;
		try {
			d = df.parse(this.date);
		} catch (ParseException e) {
			return false;
		}		
		Calendar c = Calendar.getInstance();	
		c.setTime(d);
		
		
		
		return c.getTimeInMillis()<beforeCal.getTimeInMillis();
	}

	@Override
	public boolean inLocation(int locationId) {
		
		return this.areaId == locationId;
	}

}
