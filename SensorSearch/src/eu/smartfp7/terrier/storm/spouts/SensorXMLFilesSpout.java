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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.smartfp7.terrier.sensor.Location;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class SensorXMLFilesSpout implements IRichSpout{

	
	Location[] locations;
	String[] folders;
	Map<Integer, List<File>> filesToProcess;
	Map<Integer, Iterator<File>> filesIterators; 
	
	SpoutOutputCollector _collector;
	
	int currentLocation=0;
	
	int counter = 0;
	
	public SensorXMLFilesSpout(Location[] locations, String[] folders){		
		this.locations = locations;
		this.folders = folders;
	}
	
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		filesToProcess = new HashMap<Integer, List<File>>();
		filesIterators = new HashMap<Integer, Iterator<File>>();
		initialiseFiles();		
		this._collector= collector;	
	}

	private void initialiseFiles() {
		for(int i=0;i<locations.length;i++){			
			File folder = new File(folders[i]);
			File[] files = folder.listFiles();			
			Arrays.sort(files);
			
			filesToProcess.put(i,Arrays.asList(files));
			filesIterators.put(i, Arrays.asList(files).iterator() );
		}
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextTuple() {
		Iterator<File> it =filesIterators.get(currentLocation);
		if(it.hasNext()){
			File f = it.next();
			
			try {
				String content= readFile(f);
				counter++;
				
				if(counter%10000==0) System.out.println(counter + " sensor updates processed!" );
				
				_collector.emit(new Values(content,currentLocation));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		// increment currentLocation
		currentLocation++;if(currentLocation>=locations.length) currentLocation=0;
		
		boolean more = false;  
		for(int i=0;i<locations.length;i++){
			more |= filesIterators.get(i).hasNext();
		}
		if(!more){
			_collector.emit(new Values("heartbeat",null));
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
		declarer.declare(new Fields("update","location"));
		
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
