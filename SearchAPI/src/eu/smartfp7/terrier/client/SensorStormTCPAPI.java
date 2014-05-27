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


package eu.smartfp7.terrier.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;


import eu.smartfp7.terrier.client.StormTCPAPI;
import eu.smartfp7.terrier.storm.api.json.Event;

public class SensorStormTCPAPI  extends StormTCPAPI{

	public SensorStormTCPAPI(String queryhost, int queryport) {
		super(queryhost, queryport);
	}


	public interface EventResultsCallback extends ResultsCallback<Event>
	{}
	

	@Override
	protected Serializable unmarshall(DataInputStream datin, int rank)
			throws IOException {
		int id = datin.readInt();
		String startTime = datin.readUTF();
		String activity = datin.readUTF();
		
		
		//addded 3-12-2012
		String density = datin.readUTF();
		
		
		String locationId = datin.readUTF();
		
		Event e = new Event(id, startTime, activity,density,  Integer.parseInt(locationId));
		
		e.description = activity;
		e.title=id+"\t"+startTime+"\t"+locationId;
		e.URI = "http://smartfp7.eu/events/"+id;
		
		return e;
		
	}



	
	

	
}
