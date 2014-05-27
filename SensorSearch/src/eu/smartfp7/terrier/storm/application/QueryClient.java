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


package eu.smartfp7.terrier.storm.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class QueryClient {
	
	String host;
	int port;
	
	public QueryClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void beginQuerying() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		try {
			while (!(line=br.readLine()).equalsIgnoreCase("exit")) {
				try {
					Socket socketToServer = new Socket(host, port);
					System.err.println("Send "+line);
					PrintWriter writerToServer = new PrintWriter(socketToServer.getOutputStream(), true);
					writerToServer.write(line+'\n');
					writerToServer.flush();
					writerToServer.close();
					socketToServer.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
