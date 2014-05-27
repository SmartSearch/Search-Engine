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

import java.io.Serializable;

import org.terrier.matching.ResultSet;
import org.terrier.querying.Manager;
import org.terrier.querying.SearchRequest;
import org.terrier.structures.Index;
import org.terrier.utility.ApplicationSetup;

import eu.smartfp7.terrier.client.StormAPI.ResultsCallback;

public class TerrierAPI extends StormAPI {

	Manager m;
	int queryCounter = 0;
	String path; String prefix;
	Index index;
	
	/** The weighting model used. */
	protected String wModel = ApplicationSetup.getProperty("terrier.api.model", "PL2");
	/** The matching model used.*/
	protected String mModel = ApplicationSetup.getProperty("terrier.matching", "org.terrier.matching.daat.Full");
	
	@Override
	public void init() 
	{
		System.out.println("ApplicationSetup.getProperty="+ApplicationSetup.TERRIER_VAR);
		index = (path != null || prefix != null)
				? Index.createIndex(path, prefix)
				: Index.createIndex();
		if (index == null)
			throw new RuntimeException("Index not found");
		m = new Manager(index);
	}

	@Override
	public String query(String query, ResultsCallback<?> _callback) {
		@SuppressWarnings("unchecked")
		ResultsCallback<Serializable> callback = (ResultsCallback<Serializable>) _callback;
		final String qid = String.valueOf(queryCounter++);
		final SearchRequest srq = m.newSearchRequest(qid, query);
		srq.addMatchingModel(mModel, wModel);
		m.runPreProcessing(srq);
		m.runMatching(srq);
		m.runPostProcessing(srq);
		m.runPostFilters(srq);
		ResultSet rs = srq.getResultSet();
		parseResultSet(callback, rs);
		return qid;
	}

	protected void parseResultSet(ResultsCallback<Serializable> callback,
			ResultSet rs) {
		String[] metaKeys = rs.getMetaKeys();
		for(int i=0;i<rs.getResultSize();i++)
		{
			StringBuilder s = new StringBuilder();
			for (String key : metaKeys)
				s.append(rs.getMetaItems(key)[i] + " ");
			callback.receivedResult(s.toString());
		}
		callback.finished();
	}

	@Override
	public int getIndexSize() {
		return index == null ? 0 : index.getCollectionStatistics().getNumberOfDocuments();
	}

	@Override
	public void cancel(String queryid) {
		//does nothing for this implementation
	}

}
