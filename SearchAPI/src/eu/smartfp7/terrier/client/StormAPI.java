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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class StormAPI {
	
	public interface ResultsCallback<ITEM extends Serializable>
	{
		public boolean receivedResult(ITEM retrieved);
		public void finished();
		public void error();
		public void setQueryId(String queryid);
		public void setActualQuery(String actual);
	}
	

	public abstract void init();
	public abstract String query(String query, ResultsCallback<?> callback);
	public abstract int getIndexSize();
	public abstract void cancel(String queryid);
	
	
	
	public List<Serializable> query(StringBuilder query, long time, TimeUnit t)
	{
		final List<Serializable> rtr = new ArrayList<Serializable>();
		class CallBack implements ResultsCallback<Serializable>
		{
			volatile boolean berror = false;
			volatile boolean bfinished = false;
			String actual;
			//String qid;
			
			@Override
			public boolean receivedResult(Serializable retrieved) {
				rtr.add(retrieved);
				return stillRunning();
			}

			@Override
			public void finished() {
				bfinished = true;
			}

			@Override
			public void error() {
				berror = true;
			}

			@Override
			public void setQueryId(String queryid) {
				//qid = queryid;
			}
			
			public boolean stillRunning()
			{
				return ! (berror || bfinished);
			}

			@Override
			public void setActualQuery(String actual) {
				// TODO Auto-generated method stub
				this.actual = actual;
			}

			public String getActual() {
				return actual;
			}
			
			
		}
		final CallBack c = new CallBack();
		String qid = this.query(query.toString(), c);
		
		final long targetTime = System.currentTimeMillis() + t.toMillis(time);
		while(System.currentTimeMillis() < targetTime && c.stillRunning())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { }
		}
		c.finished();
		query.delete(0, query.length());
		query.append(c.getActual());
		this.cancel(qid);
		return rtr;
	}
	
	

}