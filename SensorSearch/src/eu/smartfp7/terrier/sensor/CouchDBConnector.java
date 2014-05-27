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


package eu.smartfp7.terrier.sensor;


import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.ChangeNotification;

import eu.smartfp7.jcouchdb.SmartChangeEntry;
import eu.smartfp7.jcouchdb.SmartChangeListener;
import eu.smartfp7.jcouchdb.SmartChangeNotification;
import eu.smartfp7.jcouchdb.SmartContinuousChangesDriver;


public class CouchDBConnector {

	
	public static void main(String[] args) throws ClientProtocolException, IOException{
		
		
		 Database db = new Database("dusk.ait.gr/couchdb",80,"aitathens");		 
		 
		 
		 System.out.println(db.getStatus().getDiskSize());
		 
		 //MetaDataDocument cameraSnapShotDocument= db.getDocument(MetaDataDocument.class,"d75d0cbc0ce24380a9496d7ffc520f95");
		 
		 //System.out.println(cameraSnapShotDocument.getRdfDocument().get("smart:Crowd"));
		 
		 
		 
		 Options options = new Options();
		 options.includeDocs(true);
		 options.put("heartbeat", 10000);
		 
		 SmartContinuousChangesDriver driver = new SmartContinuousChangesDriver(db, null, 0L,options , new SmartChangeListener() {
				@Override
				public void onChange(SmartChangeNotification changeNotification) {
					List<SmartChangeEntry> changeEntries = changeNotification.getChanges();
					for(SmartChangeEntry e:changeEntries){
						System.out.println(e.toString());
					}	
				}

				@Override
				public void heartbeat() {
					System.out.println("received heartbeat!");
				}

				@Override
				public void onChange(ChangeNotification changeNotification) {
					// TODO Auto-generated method stub
					
				}
			});
		 driver.start();
	     try{
	    	 synchronized(driver){
	    		 driver.wait();
	    	}
	    }catch (InterruptedException e){
	            System.err.println("Interrupted while waiting for ContinuousChangesDriver to start");
	    }
//		 db.registerChangeListener(null, 0L,options , new ChangeListener() {
//				@Override
//				public void onChange(ChangeNotification changeNotification) {
//					List<ChangeEntry> changeEntries = changeNotification.getChanges();
//					for(ChangeEntry e:changeEntries){
//						System.out.println(e.toString());
//					}	
//				}
//			});
		 
		 //PollingResults pollingResults = db.pollChanges(since, filter, longPolling, options)
		
		 
		 //BaseDocument cameraSnapShotDocument= db.getDocument(BaseDocument.class,"d75d0cbc0ce24380a9496d7ffc520f95");
		 
		 
		 //System.out.println(cameraSnapShotDocument.getProperty("rdf:RDF"));
		 
		 /*
		HttpGet get = new HttpGet( "http://dusk.ait.gr/couchdb/smart_test_crowd" );
		
		 
        SchemeRegistry supportedSchemes = new SchemeRegistry();
        org.apache.http.conn.scheme.SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUseExpectContinue(params, false);
        HttpClientParams.setRedirecting(params, false);
        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(10));

        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 10);

        HttpContext context = new BasicHttpContext();
        ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager( params, supportedSchemes);
        DefaultHttpClient httpClient = new DefaultHttpClient(clientConnectionManager, params);
        
        AuthScope authScope = new AuthScope(System.getProperty("http.proxyHost"),Integer.parseInt(System.getProperty("http.proxyPort")));
        System.out.println(authScope.getHost());
        System.out.println(authScope.getPort());
        
        //httpClient.getCredentialsProvider().setCredentials(authScope, null);		
		
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, 
        		new HttpHost( System.getProperty("http.proxyHost"),Integer.parseInt(System.getProperty("http.proxyPort"))));
        
        HttpResponse res =  httpClient.execute( get, context );
        Response resp = new Response(res); 
		
        if (!resp.isOk()){
        	System.out.println(resp.getCode()+"");
        }else		
        	System.out.println(resp.getContentAsString());
*/
		// Running a view
		 
//		ViewResult result = db.getAllDocuments(); // same as db.view("_all_dbs");
//		for (Document d: result.getResults()) {
//		        System.out.println(d.getId());
//
//		        /*
//		                ViewResults don't actually contain the full document, only what the view
//		                returned.  So, in order to get the full document, you need to request a
//		                new copy from the database.
//		        */      Document full = db.getDocument(d.getId());
//		}
//
//		// Ad-Hoc view
//
//		ViewResult resultAdHoc = db.adhoc("function (doc) { if (doc.foo=='bar') { return doc; }}");				
	}
}
