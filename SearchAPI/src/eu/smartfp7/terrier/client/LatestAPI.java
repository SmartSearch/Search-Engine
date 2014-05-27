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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.terrier.utility.ArrayUtils;

import eu.smartfp7.terrier.client.StormAPI.ResultsCallback;
import eu.smartfp7.terrier.client.StormTCPAPI.ListenAndPrintThread;
import eu.smartfp7.terrier.storm.api.APIUtil;
import eu.smartfp7.terrier.storm.api.conf.SmartProperties;
import eu.smartfp7.terrier.storm.api.json.Observation;

public class LatestAPI{

	static int TIMEOUT = 60000;
	
	String latesthost;
	int latestport;
	int returnport = -1;
	String returnhost = null;
	
	
	int querycounter =0;
	
	HashMap<String, List<Observation>> resultsMap;
	
	private static LatestAPI one;
	
	public static LatestAPI getInstance(){
		
		if(one==null){
			
			SmartProperties props = SmartProperties.getInstance();
			String host = props.getProperty(SmartProperties.SMART_LATESTHOST);
			int port = Integer.parseInt(props.getProperty(SmartProperties.SMART_LATESTPORT)); 
			 
			one = new LatestAPI(host,port);
			
		}
		return one;
	}
	
	
	
	private LatestAPI(String latesthost, int latestport) {
		super();
		this.latesthost = latesthost;
		this.latestport = latestport;
		init();
	}
	
	public void init() {		
		querycounter=0;
		resultsMap = new HashMap<String, List<Observation>>();
		openSocket();
		assert this.returnport != -1;
		beginListening();
	}
	
//	@Override
//	public int getIndexSize()
//	{
//		return numDocsInIndex;
//	}
	

	
	
	void requestObservations(String queryid, int locationId, int n) {
		try {
			Socket socketToServer = new Socket(latesthost, latestport);
			PrintWriter writerToServer = new PrintWriter(socketToServer.getOutputStream(), true);
			writerToServer.write(ArrayUtils.join(new String[]{
					returnhost, 
					String.valueOf(returnport), 
					queryid, 
					String.valueOf(locationId),
					String.valueOf(n)}, 
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
		listenThread = new Thread(new ListenAndPrintThread(), "ListenThreadLatest");
	}
	
	


	class ListenAndPrintThread implements Runnable {

		Thread runner;

		public ListenAndPrintThread() {
			runner = new Thread(this, "ListenAndPrintThread"); // (1) Create a
																// new thread.
			System.out.println(runner.getName());
			runner.start(); // (2) Start the thread.
		}

		
		protected Observation unmarshall(DataInputStream datin)
				throws IOException{
			String dateStr= datin.readUTF();
			double density = datin.readDouble();
			int numColors = datin.readInt();
			double[] colors =null;
			if(numColors>0){
				colors = new double[numColors];
			}
			for(int i=0;i<numColors;i++){
				colors[i] = datin.readDouble();
			}
			String activity = datin.readUTF();
			
			Observation o = new Observation(dateStr, density, colors, activity);
			return o;
			
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
						int numitems = datin.readInt();
						
						List<Observation> observations = new ArrayList<Observation>();
						System.err.println("Got Response for "+queryid+" "+" with "+numitems+" observations");
						for (int i=0; i<numitems; i++) {
							Observation o = unmarshall(datin);
							observations.add(o);
						}
						
						synchronized (resultsMap) {
							resultsMap.put(queryid,observations);
						}
						
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
				} catch (Exception e) {final List<Serializable> rtr = new ArrayList<Serializable>();
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
	
	public  void latest(int locationId){
		
	}
	
	public List<Observation> latest(int locationId, int n,long time, TimeUnit t){
		
				
		String queryId = ++querycounter + "";
		this.requestObservations(queryId , locationId,n);
		final long targetTime = System.currentTimeMillis() + t.toMillis(time);
		boolean resultsReceived = false;
		while(System.currentTimeMillis() < targetTime && !resultsReceived )
		{
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { }
			synchronized (resultsMap) {
				resultsReceived = resultsMap.containsKey(queryId);
			}			
		}
		
		List<Observation> rtr = resultsMap.get(queryId);
		synchronized (resultsMap) {
			resultsMap.remove(queryId);
		}
		
		
		return rtr;	
	}
	
}


