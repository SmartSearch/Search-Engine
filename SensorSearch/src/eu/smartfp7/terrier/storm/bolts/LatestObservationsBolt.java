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


package eu.smartfp7.terrier.storm.bolts;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import eu.smartfp7.terrier.sensor.Location;
import eu.smartfp7.terrier.sensor.Observation;
import eu.smartfp7.terrier.sensor.SensorSearch;

public class LatestObservationsBolt implements IRichBolt{

	private static final int _BUFF_SIZE = 10000;
	private static final long serialVersionUID = -3718602595175059863L;

	OutputCollector _collector;
	SensorSearch sensorSearch;
	Location[] locations;
	
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	Thread listenThread = null;
	
	int observationsPort;
	int counter =0;	
	
	Map<Integer,List<Observation>> latestObservations;	
	
	public LatestObservationsBolt(int observationsPort){
		this.observationsPort = observationsPort;		
	}
	
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector= collector;
		System.setProperty("terrier.home", System.getProperty("user.dir"));
		
		
		System.out.println("TerrierHome:"+ System.getProperty("terrier.home"));
		latestObservations = new HashMap<Integer, List<Observation>>();
		
		try {
			serverSocket = new ServerSocket(this.observationsPort);
		} catch (IOException e) {
			System.err.println("Could not listen on port: "+observationsPort);
			
		}
		listenThread = new Thread(new Runnable() {
			
			@Override
			public void run() { 
				while(true){
					try {
						clientSocket = serverSocket.accept();
					} catch (IOException e) {
						System.err.println("Accept failed.");
						System.exit(1);
					}

					try {
						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						String inputLine;
						while ((inputLine = in.readLine())!=null) {
							
							final String[] parts = inputLine.split(",");
							if (parts.length<3) {
								continue;
							}
							
							final String returnhost = parts[0];
							final int returnport = Integer.parseInt(parts[1]);
							final String queryId = parts[2];
							final int locationId = Integer.parseInt(parts[3]);
							final int n = Integer.parseInt(parts[4]);
							
							if (parts.length==5) {
							
								respond( queryId, locationId,n, returnhost, returnport);
								
							}
						}
						in.close();
						clientSocket.close();
						
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
						try {
							serverSocket = new ServerSocket(observationsPort);
						} catch (IOException ei) {
							System.err.println("Could not listen on port: "+observationsPort);
							System.exit(1);
						}
						e.printStackTrace();
					}
				}
			}
		});
		listenThread.start();
		
	}

	@Override
	public void execute(Tuple input) {
		List<String> fields = input.getFields().toList();
		
		
		
		if(fields.contains("time")){
			
			//String content = input.getString(0);
			HashMap map;
			if(input.getDouble(1)==null)
			{	
				//System.out.println("heartbeat received..");
				_collector.ack(input);
				return;
			}
			/*
			map = (HashMap) input.get(0);
			EdgeNodeSnapShot s=null;
			try {
				s = ParserUtility.parseHashMap(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			*/
			String time  = input.getString(0);	
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	
			Calendar c = Calendar.getInstance();
			;
			try {
				c.setTime(df.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateStr = df.format(c.getTime());
			
			double density = input.getDouble(1);
			
			double[] colors = (double[])input.getValue(2); 
			
			int locationIndex =input.getInteger(4);
			
			Map<String, String> metaEntries = new HashMap<String, String>();			
			metaEntries.put("activity",(input.getValue(3)!=null)?input.getString(3):"");			
			
			//sensorSearch.accumulateSnapShot(dateStr,density,metaEntries,locationIndex,colors);
			
			String activity = (String)input.getValue(3);
			
			Observation o = new  Observation(dateStr, density, colors, activity);
			List<Observation> list= null;
			if(latestObservations.containsKey(locationIndex)){
				 list=latestObservations.get(locationIndex);
			}else{
				list = new ArrayList<Observation>();
				latestObservations.put(locationIndex,list);
			}
			if(list.size()>_BUFF_SIZE){
				list.remove(0);
				list.add(o);
			}else
				list.add(o);
			
			
			
		}
		_collector.ack(input);
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		
		return null;
	}
	
	
	public void respond(String queryId, int locationId, int n, String host, int port ){
		
		
		List<Observation> allObservations = this.latestObservations.get(locationId); 
		int r;
		if(n<=0) r=1;
		else if(n <= allObservations.size()) r=n;
		else r=allObservations.size();
		List<Observation> list = new ArrayList<Observation>();
		if(allObservations!=null){
			int size=allObservations.size();
			list = allObservations.subList(size-r, size); 
		}
			
		
		ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
		DataOutputStream datout = new DataOutputStream(bytearray);
		try {
			datout.writeUTF(queryId);
			datout.writeInt(list.size());
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (Observation o:list) {
			
			try {
				
				datout.writeUTF(o.getDateStr());
				datout.writeDouble(o.getDensity());
				double[] colors = o.getColors();
				if(colors!=null){
					datout.writeInt(o.getColors().length);
					for(double d:o.getColors()){
						datout.writeDouble(d);
					}
				}
				else
					datout.writeInt(0);
				String activity= o.getActivity();
				if(activity!=null)
					datout.writeUTF(activity);
				else
					datout.writeUTF("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			datout.close();
			bytearray.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			
			Socket socketToServer = new Socket(host,port);
			OutputStream ToServer = socketToServer.getOutputStream();
			PrintWriter writerToServer = new PrintWriter(ToServer, true);
				
			ToServer.write(bytearray.toByteArray());
			
			writerToServer.flush();
			writerToServer.close();
			socketToServer.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}
	
	
	
	


}
