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

import com.google.gson.Gson;

public class Example {

	static Response getExampleRespone()
	{		
		Result[] res = new Result[1];
		LocatedResult r = new LocatedResult();
		res[0] = r;
		r.rank = 1;
		r.score = 1.15;
		r.title = "New event";
		r.description = "Many tweets seen!";
		r.location = new Location();
		r.location.setLat(55.44f); 
		r.location.setLon(-4.9f);
		r.location.name = "Glasgow, roughly";
		r.media = new Media[1];
		r.media[0] = new Media("image/png", System.currentTimeMillis(), "http://localhost/sampleimage/", System.currentTimeMillis() + 3600 * 1000);
		Response rtr = new Response(res);
		return rtr;
	}
	
	static String getExampleResponseJSON()
	{
		Gson gson = new Gson();
		return gson.toJson(getExampleRespone());
	}
	
	public static void main(String[] args)
	{
		System.out.println(getExampleResponseJSON());
	}
	
}
