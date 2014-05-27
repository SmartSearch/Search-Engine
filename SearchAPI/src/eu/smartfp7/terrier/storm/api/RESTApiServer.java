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

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

import eu.smartfp7.terrier.storm.api.conf.SmartProperties;

public class RESTApiServer {
	private static URI getBaseURI() {
		int port= Integer.parseInt(SmartProperties.getInstance().getProperty("port"));
		
		return UriBuilder.fromUri("http://"+SmartProperties.getInstance().getProperty("host")+"/").port(port).build();
	}
	
	public static final URI BASE_URI = getBaseURI();
	
	protected static HttpServer startServer() throws IOException {
		System.out.println("Starting grizzly...");
		ResourceConfig rc = new PackagesResourceConfig("eu.smartfp7.terrier.storm.api");
		return GrizzlyServerFactory.createHttpServer(BASE_URI, rc);
	}
	
	public static void main(String[] args) throws IOException {
		HttpServer httpServer = startServer();
		System.out.println(String.format("Jersey app started with WADL available at "
				+ "%sapplication.wadl\nTry out %s/v1/search.json\nHit enter to stop it...",
				BASE_URI, BASE_URI));
		System.in.read();
		httpServer.stop();
	}    
}
