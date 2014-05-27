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

import eu.smartfp7.terrier.storm.api.json.Result;
import eu.smartfp7.terrier.storm.api.json.Tweet;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.Serializable;

import org.terrier.matching.ResultSet;

public class TerrierAPIj extends TerrierAPI {

	@Override
	protected void parseResultSet(
		ResultsCallback<Serializable> callback,
		ResultSet rs) 
	{
		final int n = rs.getResultSize();
		TObjectIntHashMap<String> key2index = new TObjectIntHashMap<String>();
		String [] keys = rs.getMetaKeys();
		for(int k=0;k<keys.length;k++)
		{
			key2index.put(keys[k], k+1);
		}
		for(int i=0;i<n;i++)
		{
			Tweet r = new Tweet();
			r.score = rs.getScores()[i];
			r.setRank(i+1+"");
			if (key2index.get("title") != 0)
			{
				r.title = rs.getMetaItems("title")[i];
			} else if (key2index.get("docno") != 0) {
				r.title = rs.getMetaItems("docno")[i];
			}
			if (key2index.get("description") != 0)
			{
				r.description = rs.getMetaItems("description")[i];
			}
			if (key2index.get("url") != 0)
			{
				r.URI= rs.getMetaItems("url")[i];
			}
			callback.receivedResult(r);
		}
		callback.finished();		
	}

}
