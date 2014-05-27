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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import java.util.Set;

import org.terrier.matching.ResultSet;

import eu.smartfp7.terrier.sensor.EdgeNodeSnapShot;
import eu.smartfp7.terrier.sensor.Location;
import eu.smartfp7.terrier.sensor.Observation;
import eu.smartfp7.terrier.sensor.ParserUtility;
import eu.smartfp7.terrier.sensor.SensorSearch;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class SensorIndexBolt implements IRichBolt{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3718602595175059863L;

	
	OutputCollector _collector;
	SensorSearch sensorSearch;
	Location[] locations;
	
	
	int counter =0;
	
	// 26-11-2012 : a quick fix to keep latest observations in memory and clear thew index periodically.
	Map<Integer,List<Observation>> latestObservations;		
	private static final int _BUFF_SIZE = 10000;
	long timeLastIndexClear = System.currentTimeMillis();
	long clearTime= 36*24*60*60*1000L;
	
	
	
	
	public SensorIndexBolt(Location[] locations){
		this.locations = locations;
	}
	
	public SensorIndexBolt(Location[] locations, long clearTime){
		this(locations);
		this.clearTime = clearTime;
	}
	
	
	
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector= collector;
		System.setProperty("terrier.home", System.getProperty("user.dir"));
		
		// TODO Make locations a configurable setting
		/*
		Location[] locations = new Location[2];
		locations[0] = new Location("1,","AITSMARTLab", 37.942882, 23.870308);
		locations[1] = new Location("2","AITAthens", 37.942882, 23.870308);
		*/
		System.out.println("TerrierHome:"+ System.getProperty("terrier.home"));
		
		sensorSearch = new SensorSearch(locations);
		System.out.println("Sensor search created..");
		
		latestObservations = new HashMap<Integer, List<Observation>>();
		
	}

	@Override
	public void execute(Tuple input) {
		List<String> fields = input.getFields().toList();
		
		
		
		if(fields.contains("time")){
			
			//String content = input.getString(0);
			HashMap map;
			if(input.getDouble(1)==null)
			{	
				System.out.println("heartbeat received..");
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
			
			// store in Buffer..
			storeInBuffer(dateStr,density,metaEntries,locationIndex,colors);
			
			sensorSearch.accumulateSnapShot(dateStr,density,metaEntries,locationIndex,colors);
			
			
			if(++counter%10000==0) System.out.println(counter + " sensor updated indexed!");
			
			
			
			
		}else
		
		if(fields.contains("update")){
			
			String content = input.getString(0);
			
			if(content.equals("heartbeat")){
				System.out.println("heartbeat received..");
				_collector.ack(input);
				return;
			}
			
			EdgeNodeSnapShot s=null;
			try {
				s = ParserUtility.parseShort(
						new ByteArrayInputStream(input.getString(0).getBytes()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			int locationIndex =input.getInteger(1);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateStr = df.format(s.getTimestamp().getTime());
			Map<String, String> metaEntries = new HashMap<String, String>();			
			metaEntries.put("activity",s.getText());			
			sensorSearch.accumulateSnapShot(dateStr, s.getCrowdData().getDensity(),metaEntries,locationIndex);
			
			if(++counter%10000==0) System.out.println(counter + " sensor updated indexed!");
			
		}
		else if (fields.contains("query")){
			String querytext = (String)input.getStringByField("query");
			String queryid = (String)input.getStringByField("queryid");
			String host = (String)input.getStringByField("host");
			int port = Integer.parseInt((String)input.getStringByField("port"));
			
			
			if(querytext.equals("BLANK"))
				querytext ="crowd";
			ResultSet rs =sensorSearch.search(querytext);
			
			
			ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
			DataOutputStream datout = new DataOutputStream(bytearray);
			
			try{
				datout.writeUTF(queryid);
				datout.writeUTF(querytext);
				int k=0;
				int resultsize = 0;
				for(int id:rs.getDocids()){
					if (rs.getScores()[k++]>-100)
						resultsize++;
					else break;
				}
				datout.writeInt(resultsize);
				
				for(int i=0;i< resultsize; i++){
					int id = rs.getDocids()[i];
					datout.writeInt(id);
					datout.writeUTF(sensorSearch.getMetadata().getItem(SensorSearch.TIMESTAMP_KEY, id));
					datout.writeUTF(sensorSearch.getMetadata().getItem("activity", id));
					datout.writeUTF(sensorSearch.getMetadata().getItem("density", id));
					datout.writeUTF(sensorSearch.getLocation(id).getId());
				}
				datout.close();
				bytearray.close();
			}catch(IOException e){
				e.printStackTrace();
			}catch (Exception e) {
				
			}
			
			try {
				
				System.out.println("Host of query request: " + host);
				System.out.println("Port of query request: " + port);
				
				Socket socketToServer = new Socket(host, port);
				OutputStream ToServer = socketToServer.getOutputStream();
				PrintWriter writerToServer = new PrintWriter(ToServer, true);
				
				
				
				System.err.println("Echoing "+queryid+" "+querytext);				
				ToServer.write(bytearray.toByteArray());
				
				writerToServer.flush();
				writerToServer.close();
				socketToServer.close();
			}catch (UnknownHostException e){
				e.printStackTrace();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		
		// 26-11-12: clear index periodically and keep latest.
		if(System.currentTimeMillis()>timeLastIndexClear+clearTime){
			
			resetIndex();
			
			timeLastIndexClear = System.currentTimeMillis();
		}
		
		
		
		
		_collector.ack(input);
	}

	private void resetIndex() {
		sensorSearch.clear();
		Set<Integer> locationIds =latestObservations.keySet();
		
		for(int i: locationIds){
			
			for(Observation o:latestObservations.get(i)){
				sensorSearch.accumulateSnapShot(o.getDateStr(),o.getDensity(),o.getMetaEntries(),i,o.getColors());
				
			}
			
			
		}
		
	}

	private void storeInBuffer(String dateStr, double density,
		Map<String, String> metaEntries, int locationIndex, double[] colors) {
		Observation o = new  Observation(dateStr, density, colors, metaEntries);
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

}
