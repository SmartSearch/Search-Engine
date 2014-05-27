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

import java.util.Map;

import org.terrier.realtime.MemoryIndex;
import org.terrier.realtime.memory.MemoryCollectionStatistics;
import org.terrier.realtime.memory.MemoryDocumentIndex;
import org.terrier.realtime.memory.MemoryInvertedIndex;
import org.terrier.realtime.memory.MemoryLexicon;
import org.terrier.realtime.memory.MemoryMetaIndex;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class EchoBolt implements IRichBolt{

	OutputCollector _collector;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		this._collector = collector;
		System.setProperty("terrier.home", "/users/dyaa/realtimeTerrier");
		System.out.print("1");
		MemoryLexicon lexicon = new MemoryLexicon();
		System.out.print("2");
		MemoryDocumentIndex document = new MemoryDocumentIndex();
		System.out.print("3");
		MemoryInvertedIndex inverted = new MemoryInvertedIndex(lexicon, document);
		System.out.print("4");
		MemoryMetaIndex metadata = new MemoryMetaIndex();
		System.out.print("5");
		MemoryCollectionStatistics stats = new MemoryCollectionStatistics(0, 0, 0, 0, new long[] { 0 });
		System.out.print("6");
		
	}

	@Override
	public void execute(Tuple input) {
		
		System.out.println("received ... ");
		
		_collector.ack(input);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
