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

public class FederatedResult {

	
	Query query;
	Response twitter;
	Response smart;
	Response rssfeeds;
	
	
	
	public Query getQuery() {
		return query;
	}
	public void setQuery(Query query) {
		this.query = query;
	}
	public Response getTwitter() {
		return twitter;
	}
	public void setTwitter(Response twitter) {
		this.twitter = twitter;
	}
	public Response getSmart() {
		return smart;
	}
	public void setSmart(Response smart) {
		this.smart = smart;
	}
	public FederatedResult(Query query, Response twitter, Response smart) {
		super();
		this.query = query;
		this.twitter = twitter;
		this.smart = smart;
	}
	
	public FederatedResult(Query query, Response twitter, Response smart, Response rssfeeds) {
		super();
		this.query = query;
		this.twitter = twitter;
		this.smart = smart;
		this.rssfeeds= rssfeeds;
	}
	
	public FederatedResult() {
		super();
	}
	public Response getRssfeeds() {
		return rssfeeds;
	}
	public void setRssfeeds(Response rssfeeds) {
		this.rssfeeds = rssfeeds;
	}
	
	
	
	
	
	
}
