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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.DesignDocument;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.View;
import org.jcouchdb.document.ViewResult;
import org.svenson.JSONParser;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Response;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.DesignDocument;
import org.svenson.JSON;
import org.svenson.JSONParser;

public class CouchDBTimeCrowdPlotter {

	public static void main(String[] args) throws IOException{
		
		Database db = new Database("dusk.ait.gr/couchdb",80,"smart_test_crowd");
		
		DesignDocument designDocument = db.getDesignDocument("getTimeNdensity");
		
		View view  =designDocument.getView("timeNdensity");
		
		ViewResult<Object> vr= db.query("_design/getTimeNdensity/_view/timeNdensity", Object.class, new Options(), new JSONParser(),null);
		
		
		FileWriter fileWriter = new FileWriter(new File(args[0])); 
		
		for(ValueRow<Object> v: vr.getRows()){
			System.out.println(v.getKey()+","+v.getValue());
			fileWriter.append(v.getKey()+","+v.getValue()+"\n");
		}	
		
		
		
	}
}
