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
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;


import com.google.gson.Gson;

import eu.smartfp7.terrier.storm.api.conf.SmartProperties;
import eu.smartfp7.terrier.storm.api.json.Event;
import eu.smartfp7.terrier.storm.api.json.Query;
import eu.smartfp7.terrier.storm.api.json.QueryResponsePair;
import eu.smartfp7.terrier.storm.api.json.Response;
import eu.smartfp7.terrier.storm.api.json.Result;

/** see http://jersey.java.net/nonav/documentation/latest/user-guide.html#d4e47 */

/**
 * @author dyaa
 *
 */
@Path("/v1/search.json")
public class Search {

	
	
	
	
	/**
	 * Http end point for issuing a query.
	 * http://hostname/v1/search.json?q=my+search
	 * 
	 * @param q
	 * @return
	 */
	@GET
	@Produces("application/json")
	public String doSearchJSON(
		@QueryParam("q") String q) {
				
		if (q == null || q.length() == 0)
		{
			throw new InvalidRequestException("No query (q) was specified\n");
		}
		
		//List<Serializable> rtr = APIUtil.getAPI().query(q,20000, TimeUnit.MILLISECONDS);
		StringBuilder qbuilder = new StringBuilder();

		q = q.replaceAll("[^\\w]", " ");		

		qbuilder.append(q);
		List<Serializable> rtr = APIUtil.getInstance().getAPI().query(qbuilder,20000, TimeUnit.MILLISECONDS);
		Result[] r = new Result[rtr.size()];
		int i=0;
		for(Serializable item : rtr)
		{
			r[i++] = (Result)item;
		}
			
		Query query = new Query(q);
		query.setTrending(qbuilder.toString());
		return new Gson().toJson(new QueryResponsePair(query, new Response(r)));
	}
	
}
