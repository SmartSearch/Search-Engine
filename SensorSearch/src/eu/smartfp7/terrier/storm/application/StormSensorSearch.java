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


import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;
import eu.smartfp7.conf.SensorProperties;
import eu.smartfp7.terrier.sensor.Location;
import eu.smartfp7.terrier.sensor.SensorSearch;
import eu.smartfp7.terrier.storm.bolts.EchoBolt;
import eu.smartfp7.terrier.storm.bolts.LatestObservationsBolt;
import eu.smartfp7.terrier.storm.bolts.SensorIndexBolt;
import eu.smartfp7.terrier.storm.spouts.CouchDBSpout;
import eu.smartfp7.terrier.storm.spouts.QuerySpout;
import eu.smartfp7.terrier.storm.spouts.SensorXMLFilesSpout;
import eu.smartfp7.terrier.storm.spouts.StreamingCouchDBSpout;

public class StormSensorSearch {
	public static void main(String[] args){
		
		
		System.setProperty("terrier.home", System.getProperty("user.dir"));		
		System.setProperty("ignore.low.idf.terms","false");
		System.setProperty("indexer.meta.forward.keys",SensorSearch.TIMESTAMP_KEY+",activity,density");
		System.setProperty("indexer.meta.forward.keylens","30,500,100");
		
		System.setProperty("matching.retrieved_set_size","0");
		
		Config config = new Config();
		config.setNumWorkers(10);
		config.setDebug(false);
		
		

		int querySpoutInPort = Integer.parseInt(args[0]);
		int latestInPort = Integer.parseInt(args[1]);
//		boolean switchCMDServer = Boolean.parseBoolean(args[2]);
		
//		SimpleServer server = null;
//		
//		
//		if(switchCMDServer){
//			server = new SimpleServer(querySpoutOutPort);
//			server.openSocket();
//			server.beginListening();
//		}
		
		TopologyBuilder builder = new TopologyBuilder();
		
		
		String edgeNodes = SensorProperties.getInstance().getProperty(SensorProperties.EDGE_NODES);
		
		String[] edgeDescriptions = edgeNodes.split(";");
		Location[] locations = new Location[edgeDescriptions.length];
		
		for(int i=0;i<edgeDescriptions.length;i++){
			
			String d = edgeDescriptions[i];
			String comps[]= d.split(",");
			Location location = new Location(i+"", comps[0], new Double(comps[1]), new Double(comps[2]),comps[3],comps[4]);
			
			locations[i]= location;
		}
		
		
		/*
		
		locations[0] = new Location("1","AITSMARTLab", 37.942882, 23.870308);
		locations[1] = new Location("2","AITAthens", 37.942882, 23.870308);
		
		*/
		
		//builder.setSpout("sensorupdates", new SensorXMLFilesSpout(locations, 
			//	new String[]{"/users/tr.dyaa/AITSmartLab","/users/tr.dyaa/AITathens"}),1);
		//builder.setSpout("sensorupdates", new CouchDBSpout(locations));
		for(int i=0; i<locations.length;i++){
			builder.setSpout("sensorupdates"+i, new StreamingCouchDBSpout(locations[i]));
		}
		
		builder.setSpout("queryrequest", new QuerySpout(querySpoutInPort),1);
		
		
		//builder.setBolt("echo", new EchoBolt()).allGrouping("sensorupdates");
		BoltDeclarer dec= builder.setBolt("sensorindex",new SensorIndexBolt(locations));
		
		for(int i=0; i<locations.length;i++){
			dec.allGrouping("sensorupdates"+i);
		}
		dec.allGrouping("queryrequest");
		
		dec = builder.setBolt("latest", new LatestObservationsBolt(latestInPort));
		for(int i=0; i<locations.length;i++){
			dec.allGrouping("sensorupdates"+i);
		}
		LocalCluster cluster = new LocalCluster();
		
		StormTopology topology = builder.createTopology();
		
		cluster.submitTopology("sensors", config, topology);
		
		QueryClient client = new QueryClient("localhost", querySpoutInPort);
		client.beginQuerying();
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		
//		if(server!=null){
//			//cluster.killTopology("sensors");
//			//cluster.shutdown();		
//			server.listenThread.destroy();
//		}
		
		
	}
}
