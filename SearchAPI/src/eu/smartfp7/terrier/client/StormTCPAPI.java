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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.terrier.utility.ArrayUtils;

import com.sun.research.ws.wadl.Application;

import eu.smartfp7.terrier.storm.api.conf.SmartProperties;

public abstract class StormTCPAPI extends StormAPI {

	static int TIMEOUT = 60000;
	
	String queryhost;
	int queryport;
	int returnport = -1;
	String returnhost = null;
	volatile int querycounter;
	volatile int numDocsInIndex;
	
	Map<String,ResultsCallback<?>> callbacks= new HashMap<String, StormAPI.ResultsCallback<?>>();
	
	
	public StormTCPAPI(String queryhost, int queryport) {
		super();
		this.queryhost = queryhost;
		this.queryport = queryport;
	}
	
	@Override
	public void init() {		
		querycounter=20000;
		openSocket();
		assert this.returnport != -1;
		beginListening();
	}
	
	@Override
	public int getIndexSize()
	{
		return numDocsInIndex;
	}
	
	@Override
	public String query(String query, ResultsCallback<?> callback)
	{
		String queryid = String.valueOf(querycounter++);
		callback.setQueryId(queryid);
		callbacks.put(queryid, callback);	
		stormQuery(queryid, query);
		return queryid;
	}
	
	void stormQuery(String queryid, String query) {
		try {
			Socket socketToServer = new Socket(queryhost, queryport);
			PrintWriter writerToServer = new PrintWriter(socketToServer.getOutputStream(), true);
			writerToServer.write(ArrayUtils.join(new String[]{
					queryid, 
					query, 
					returnhost, 
					String.valueOf(returnport), 
					String.valueOf(TIMEOUT) }, 
				',') + '\n');
			writerToServer.flush();
			writerToServer.close();
			socketToServer.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void cancel(String queryid) {
		try {
			Socket socketToServer = new Socket(queryhost, queryport);
			PrintWriter writerToServer = new PrintWriter(socketToServer.getOutputStream(), true);
			writerToServer.write(queryid+","+"##INVLIDATE##"+",-1"+'\n'); // time out after 60 seconds..
			writerToServer.flush();
			writerToServer.close();
			socketToServer.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	Thread listenThread = null;

	void openSocket() {
		try {
			serverSocket = new ServerSocket(0);
			//System.err.println("Opened socket: "+serverSocket.toString());
			this.returnport = serverSocket.getLocalPort();
			this.returnhost = "localhost";
			
			System.out.println(this.returnhost);
			System.out.println(this.returnport);
			
		} catch (IOException e) {
			System.err.println("Could not create listening socket" + e);
			System.exit(1);
		}
		assert this.returnport != -1;
		assert this.returnhost != null && this.returnhost.length() > 0;
	}
	
	void beginListening() {
		listenThread = new Thread(new ListenAndPrintThread(), "ListenThread1");
	}
	
	protected abstract Serializable unmarshall(DataInputStream datin, int rank)
			throws IOException;


	class ListenAndPrintThread implements Runnable {

		Thread runner;

		public ListenAndPrintThread() {
			runner = new Thread(this, "ListenAndPrintThread"); // (1) Create a
																// new thread.
			System.out.println(runner.getName());
			runner.start(); // (2) Start the thread.
		}

		public void run() {
			while (true) {

				try {
					clientSocket = serverSocket.accept();
					
				} catch (IOException e) {
					System.err.println("Accept failed.");
					System.exit(1);
				}

				try {
					// PrintWriter out = new
					// PrintWriter(clientSocket.getOutputStream(), true);
					InputStream stream = clientSocket.getInputStream();
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte buffer[] = new byte[1024];
					for(int s; (s=stream.read(buffer)) != -1; )
					{
					  baos.write(buffer, 0, s);
					}
					byte result[] = baos.toByteArray();
				    try {
						DataInputStream datin = new DataInputStream(new ByteArrayInputStream(result));
						String queryid = datin.readUTF();
						String query = datin.readUTF();
						
						@SuppressWarnings("unchecked")
						ResultsCallback<Serializable> callback = (ResultsCallback<Serializable>) callbacks.get(queryid);
						
						// added 7-6-2012 request the number of tweets in the index.						
						
						
						// A hack to deal with Twitter/Sensor bolts
						// TODO: make it uniform
						if(queryport==Integer.parseInt(SmartProperties.getInstance().getProperty(SmartProperties.SMART_TWEETPORT))  )
							numDocsInIndex = datin.readInt();
						
						
						int numitems = datin.readInt();
						
						System.err.println("Got Response for "+queryid+" "+query+" with "+numitems+" items");
						for (int i=0; i<numitems; i++) {
							Serializable o = unmarshall(datin, i);
							callback.receivedResult(o);
						}
						callback.setActualQuery(query);
						callback.finished();
						
						
						datin.close();
						stream.close();
						clientSocket.close();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					/*
					 * out.close(); in.close(); clientSocket.close();
					 * serverSocket.close();
					 */
				} catch (Exception e) {
					if (clientSocket != null) {
						try {
							clientSocket.close();
							serverSocket.close();
						} catch (IOException e1) {
						}
					}
					openSocket();
					e.printStackTrace();
				}
			}
		}
		
	}

	
}
