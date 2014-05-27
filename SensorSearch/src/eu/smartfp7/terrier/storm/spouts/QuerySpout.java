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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * Query spout that takes in queries from StormCaller
 * Basic query: qid,query
 * Running query: qid,query,runningTime
 * End Running: qid,query,-1
 * @author EbonBlade
 *
 */
public class QuerySpout implements IRichSpout{

	SpoutOutputCollector _collector;
	String host;
	int port;
	int queryport;

	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	//Thread listenThread = null;
	
	@Deprecated
	public QuerySpout(String outhost, int outport, int inport) {
		this.host = outhost;
		this.port = outport;
		queryport = inport;
	}
	
	public QuerySpout( int inport) {
		queryport = inport;
	}
	
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
		
		try {
			serverSocket = new ServerSocket(queryport);
		} catch (IOException e) {
			System.err.println("Could not listen on port: "+queryport);
			System.exit(1);
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextTuple() {
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
				/* Basic query: qid,query,returnhost,returnport
				 * Running query: qid,query,returnhost,returnport,runningTime
				 * End Running: qid,query,returnhost,returnport,-1
				 */
				final String[] parts = inputLine.split(",");
				if (parts.length<4) {
					continue;
				}
				
				final String qid = parts[0];
				final String query = parts[1];
				final String returnhost = parts[2];
				final String returnport = parts[3];
				if (parts.length==4) {
					 _collector.emit(new Values(returnhost, returnport, qid, query, null));
				}
				else
				{
					final String time = parts[4];
					_collector.emit(new Values(returnhost, returnport, qid, query, time));
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
				serverSocket = new ServerSocket(queryport);
			} catch (IOException ei) {
				System.err.println("Could not listen on port: "+queryport);
				System.exit(1);
			}
			e.printStackTrace();
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
		declarer.declare(new Fields("host", "port", "queryid", "query", "runningTime"));
		
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
