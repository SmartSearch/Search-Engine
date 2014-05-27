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

import eu.smartfp7.terrier.storm.api.json.RSSFeed;

public class RSSStormTCPAPI extends StormTCPAPI {

	public RSSStormTCPAPI(String queryhost, int queryport) {
		super(queryhost, queryport);
	}

	@Override
	protected Serializable unmarshall(DataInputStream datin, int rank)
			throws IOException {
		
		
		int id = datin.readInt();
		String title = datin.readUTF();
		String description = datin.readUTF();
		String link = datin.readUTF();
		String permalink = datin.readUTF();
		String dateStr = datin.readUTF();
		int areaId = Integer.parseInt(datin.readUTF());
		
		
		return new RSSFeed(title, description, link, permalink, dateStr, areaId);
	}

}
