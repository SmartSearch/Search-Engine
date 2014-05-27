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


package eu.smartfp7.terrier.storm.spouts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.DesignDocument;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.View;
import org.jcouchdb.document.ViewResult;
import org.svenson.JSONParser;

import eu.smartfp7.terrier.sensor.Location;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class CouchDBSpout  implements IRichSpout {
	Location[] locations;
	
	final String VIEW_URL= "_design/get_data/_view/by_date";
	
	
	SpoutOutputCollector _collector;
	
	int currentLocation=0;
	
	int counter = 0;
	
	
	Iterator<ValueRow> iterator;
	
	List<ValueRow> alldata;
	List<Integer> indices = new ArrayList<Integer>();
	
	Iterator<Integer> indicesIterator;
	int currentIndex = -1;
	
	public CouchDBSpout(Location[] locations){super();this.locations = locations;}
	
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		initialise();		
		this._collector= collector;	
	}
	
	protected void initialise(){
		
		alldata = new ArrayList<ValueRow>();
		 
		for(Location l:locations){
			
			
			Database db = new Database(l.getHost(), 80,
					l.getDbname());
			
			
			
			ViewResult<Object> vr = db.query(
					VIEW_URL, Object.class,
					new Options(), new JSONParser(),null);
	
			int count =0;
			for (ValueRow<Object> v : vr.getRows()) {
				//indexSnapShot(v);
				alldata.add(v);
				count++;
			}
			indices.add(count);
		}
		iterator = alldata.iterator();
		indicesIterator = indices.iterator();
		if(indicesIterator.hasNext())
			currentIndex = indicesIterator.next();
		else currentIndex =-1;
	}


	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextTuple() {
		
		if(iterator.hasNext()){
			
			
			
			try {
				counter++;
				if(counter==currentIndex){
					currentLocation++;
					if(indicesIterator.hasNext()){
						currentIndex= indicesIterator.next();
						
					}else
						currentIndex=-1;
				}
				
				if(counter%10000==0) System.out.println(counter + " sensor updates processed!" );
				
				HashMap hashMap= (HashMap) iterator.next().getValue();
				
				HashMap crowdHashMap = (HashMap) hashMap.get("crowd");
				double density= new Double((String) crowdHashMap.get("density"));
				
				String time  = (String)crowdHashMap.get("time");	
				
				double[] colors=null;
				
				if(crowdHashMap.containsKey("color"))
				{
					ArrayList colorList = (ArrayList)crowdHashMap.get("color");
					colors = new double[colorList.size()];
					for(int i=0;i<colorList.size();i++){
						colors[i] = new Double((String)colorList.get(i));
					}
				}
				
				String activity = null;
				if(hashMap.containsKey("activity")) {
					activity = (String)((HashMap)hashMap.get("activity")).get("name");
				}
				
				
				_collector.emit(new Values(time,density, colors, activity,currentLocation));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		// increment currentLocation
		//currentLocation++;if(currentLocation>=locations.length) currentLocation=0;
		
		
		
		
		else{
			_collector.emit(new Values("heartbeat",null,null,null,null));
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private String readFile(File file) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line =null;
		StringBuilder builder = new StringBuilder();
		while((line = br.readLine())!=null){
			builder.append(line+"\n");
		}
		br.close();
		return builder.toString();
		
	}

	@Override
	public void ack(Object msgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(Object msgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("time","density","color","activity","location"));
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

}
