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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.Gson;

import eu.smartfp7.terrier.client.LatestAPI;
import eu.smartfp7.terrier.storm.api.conf.SmartProperties;
import eu.smartfp7.terrier.storm.api.json.Observation;
import eu.smartfp7.terrier.storm.api.json.ObservationRespose;
import eu.smartfp7.terrier.storm.api.json.Query;
import eu.smartfp7.terrier.storm.api.json.QueryResponsePair;
import eu.smartfp7.terrier.storm.api.json.Response;
import eu.smartfp7.terrier.storm.api.json.Result;


@Path("/v1/latest.json")
public class Latest {

	
	static HashMap<String, Integer > locationsMap = new HashMap<String, Integer>();;
	
	static{
		String edgeNodes = SmartProperties.getInstance().getProperty(SmartProperties.EDGE_NODES);
		
		String[] edgeDescriptions = edgeNodes.split(";");		
		for(int i=0;i<edgeDescriptions.length;i++){				
			String d = edgeDescriptions[i];
			String comps[]= d.split(",");
			locationsMap.put(comps[0].toLowerCase(), i);
		}
	}
	
	
	/**
	 * Http end point for obtaining latest observations
	 * 
	 * @param location
	 * @return
	 */
	@GET
	@Produces("application/json")
	public String doLatest(
		@QueryParam("location") String location, @QueryParam("n") String numberOfObservations) {
				
		if (location== null || location.length() == 0)
		{
			throw new InvalidRequestException("No location (location) was specified\n");
		}
		
		if(!locationsMap.containsKey(location.toLowerCase())){
			throw new InvalidRequestException("Location "+ location +" is not registerd with the search engine"+"\n");
		}
		
		int n=0;
		try{
			n= Integer.parseInt(numberOfObservations);
		}catch (NumberFormatException e) {
			
		}
		
		int locationId = locationsMap.get(location.toLowerCase());
		
		//List<Serializable> rtr = APIUtil.getAPI().query(q,20000, TimeUnit.MILLISECONDS);
		//List<Serializable> rtr = APIUtil.getInstance().getAPI().query(q,20000, TimeUnit.MILLISECONDS);
		List<Observation> rtr = LatestAPI.getInstance().latest(locationId,n,20000, TimeUnit.MILLISECONDS);
		Observation[] r = new Observation[rtr.size()];
		int i=0;
		for(Observation item : rtr)
		{
			r[i++] = item;
		}
		
		return new Gson().toJson(new ObservationRespose(r, location));
	}
	
	
}
