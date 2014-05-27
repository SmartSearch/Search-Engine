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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.ChangeNotification;
import org.jcouchdb.document.ValueRow;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import eu.smartfp7.jcouchdb.SmartChangeListener;
import eu.smartfp7.jcouchdb.SmartChangeNotification;
import eu.smartfp7.jcouchdb.SmartContinuousChangesDriver;
import eu.smartfp7.terrier.sensor.Location;

public class StreamingCouchDBSpout implements IRichSpout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Location location;
	SpoutOutputCollector _collector;
	int counter = 0;	
	

	
	List<SmartChangeNotification> buffer;
	AtomicInteger beats;
	
	public StreamingCouchDBSpout(Location location){
		super();
		this.location = location;
	}
	
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		initialise();		
		this._collector= collector;	
		
		
	}
	
	protected void initialise(){
		
		//alldata = new ArrayList<ValueRow>();
		 buffer = new ArrayList<SmartChangeNotification>();
		
		beats = new AtomicInteger(0);
			
		Database db = new Database(location.getHost(), 80,
				location.getDbname());
			
		Options options = new Options();
		options.includeDocs(true);
		options.put("heartbeat", 10000);
		 
		SmartContinuousChangesDriver driver = new SmartContinuousChangesDriver(db, null, 0L,options , new SmartChangeListener() {
			@Override
			public  void onChange(SmartChangeNotification changeNotification) {
				
				if(changeNotification==null){
					return;
				}
				synchronized (buffer) {
					buffer.add(changeNotification);
				}
					
			}

			@Override
			public void heartbeat() {
				beats.incrementAndGet();
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
			
	}


	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public  void nextTuple() {
		synchronized (buffer) {
			
		
			if(!buffer.isEmpty()){
				
				SmartChangeNotification not= buffer.remove(0);
				
				HashMap doc = not.getDoc();
				if(doc.containsKey("data")){
					HashMap rdfMap  = (HashMap) doc.get("data"); 
					HashMap crowdHashMap = (HashMap) rdfMap.get("crowd");
					
					double density=0.0;
					if(crowdHashMap.get("density") instanceof BigDecimal){
						BigDecimal bd = (BigDecimal) crowdHashMap.get("density");
						density= bd.doubleValue();
					
					}
					
					List<Integer> indices = new ArrayList<Integer>();
					//String time  = (String)crowdHashMap.get("time");	
					String time  = (String)doc.get("_id");
					
					
					double[] colors=null;
					
					if(crowdHashMap.containsKey("color"))
					{
						ArrayList colorList = (ArrayList)crowdHashMap.get("color");
						colors = new double[colorList.size()];
						for(int i=0;i<colorList.size();i++){
							
							//colors[i] = new Double((String)colorList.get(i));
							BigDecimal bd = (BigDecimal)  colorList.get(i);
							
							colors[i] = bd.doubleValue();
						}
					}
					
					String activity = null;
					if(rdfMap.containsKey("activity")) {
						activity = (String)((HashMap)rdfMap.get("activity")).get("name");
					}
					
					
					_collector.emit(new Values(time,density, colors, activity,Integer.parseInt(location.getId())));
					
				}
				
				
			}else if(beats.get()>0){
				
				_collector.emit(new Values("heartbeat",null,null,null,null));
				beats.decrementAndGet();
				
			}
		}
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
