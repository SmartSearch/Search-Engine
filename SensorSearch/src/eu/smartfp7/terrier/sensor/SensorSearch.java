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


package eu.smartfp7.terrier.sensor;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.DesignDocument;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.View;
import org.jcouchdb.document.ViewResult;
import org.svenson.JSONParser;
import org.terrier.matching.ResultSet;
import org.terrier.querying.Manager;
import org.terrier.querying.SearchRequest;
import org.terrier.realtime.MemoryIndex;
import org.terrier.realtime.memory.MemoryCollectionStatistics;
import org.terrier.realtime.memory.MemoryInvertedIndex;
import org.terrier.realtime.memory.MemoryLexicon;
import org.terrier.realtime.memory.MemoryLexiconEntry;
import org.terrier.realtime.subindex.MemoryDocumentIndexMap;
import org.terrier.realtime.subindex.MemoryMetaIndexMap;
import org.terrier.structures.indexing.DocumentPostingList;

public class SensorSearch extends Manager{

	protected MemoryInvertedIndex inverted;
	protected MemoryMetaIndexMap metadata;
	protected MemoryDocumentIndexMap document;
	MemoryLexicon memlex;
	MemoryCollectionStatistics memcollstats;

	double crowdSampleUnit = 0.01;

	public static String CROWD_TOKEN = "crowd";

	public static String TIMESTAMP_KEY = "time";

	int qid = 0;
	
	/**
	 * An array of Locations representing indexed locations.
	 */
	Location[] locations;
	
	/**
	 * 
	 */
	final int BLOCK_SIZE = 1000000;
	
	
	public long getReferenceDate() {
		return referenceDate;
	}

	double densitySum=0.0d;
	int snapsCount = 0;
	long firstSnapMilis = 0;
	String firstSnapDate="";
	long referenceDate; // first date we indexed
	
	double[] densitySums;
	int[] snapsCounts;
	long[] firstSnapsMilis;
	String[] firstSnapsDate;
	
	Map<String, String> accumulatedMap = new HashMap<String, String>();
	
	Object[] accumulatedMaps;
	
	/**
	 * this method clears the index and 
	 */
	public void clear(){
		
		// reinitialise the index structures.
		document = new MemoryDocumentIndexMap();		
		metadata = new MemoryMetaIndexMap();		
		memlex = new MemoryLexicon();		
		inverted = new MemoryInvertedIndex(memlex, document);		
		memcollstats = new MemoryCollectionStatistics(0, 0, 0, 0,new long[] { 0 });
		
		densitySum=0.0d;
		snapsCount = 0;
		firstSnapMilis = 0;
		firstSnapDate="";
		
		densitySums = new double[locations.length];
		snapsCounts = new int[locations.length];
		firstSnapsMilis = new long[locations.length];
		accumulatedMaps = new Object[locations.length];
		firstSnapsDate = new String[locations.length];
		for(int i=0;i<accumulatedMaps.length;i++){
			accumulatedMaps[i] = new HashMap<String, String>();
		}
		
		
		
	}
	

	public SensorSearch() {
		
		super(new MemoryIndex());
		
		
		System.out.print("1");
		document = new MemoryDocumentIndexMap();
		System.out.print("2");
		metadata = new MemoryMetaIndexMap();
		System.out.print("3");
		memlex = new MemoryLexicon();
		System.out.print("4");
		inverted = new MemoryInvertedIndex(memlex, document);
		System.out.print("5");
		memcollstats = new MemoryCollectionStatistics(0, 0, 0, 0,
				new long[] { 0 });
		System.out.print("6");
		
		
	}
	
	public SensorSearch(Location[] locations) {
		this();
		this.locations = locations;
		densitySums = new double[locations.length];
		snapsCounts = new int[locations.length];
		firstSnapsMilis = new long[locations.length];
		accumulatedMaps = new Object[locations.length];
		firstSnapsDate = new String[locations.length];
		for(int i=0;i<accumulatedMaps.length;i++){
			accumulatedMaps[i] = new HashMap<String, String>();
		}
		
		
		
	}
	
	public Location getLocation(int docid) throws Exception{
		if(docid/BLOCK_SIZE>= locations.length)
			throw new Exception();
		return locations[docid/BLOCK_SIZE];
	}

	public Location[] getLocations() {
		return locations;
	}
	
	
	public ResultSet search(String query) {

		MemoryIndex index = new MemoryIndex(memlex, document, inverted,
				metadata, memcollstats);
		Manager man = new Manager(index);

		SearchRequest srq = man.newSearchRequest(++qid + "", query);
		srq.setControl("decorate", "off");
		srq.setControl("qe", "off");

		srq.addMatchingModel("org.terrier.matching.daat.Full", "DirichletLM");
		
		
		
		man.runPreProcessing(srq);
		
		man.runMatching(srq);
		man.runPostProcessing(srq);
		man.runPostFilters(srq);
		
		return srq.getResultSet();

	}

	/**
	 * Process the meta-data in one batch and index it.
	 */
	public void batchIndex() {

		long start= System.currentTimeMillis();
		
		Database db = new Database("dusk.ait.gr/couchdb", 80,
				"smart_test_crowd");
		DesignDocument designDocument = db.getDesignDocument("getTimeNdensity");
		View view = designDocument.getView("timeNdensity");
		ViewResult<Object> vr = db.query(
				"_design/getTimeNdensity/_view/timeNdensity", Object.class,
				new Options(), new JSONParser(), null);

		for (ValueRow<Object> v : vr.getRows()) {
			//indexSnapShot(v);
			accumulateValue(v);
		}
		
		long end  = System.currentTimeMillis();
		double length = (end-start)/1000.0;
		System.out.println("Indexing time : "+ length);
	}
	
	private void accumulateValue(ValueRow<Object> v) {
		
		String date = (String)v.getKey();
		double density = Double.parseDouble(v.getValue().toString());
		accumulateSnapShot(date, density, new HashMap<String, String>());
	}	
	
	public void accumulateSnapShot(String date, double density, Map<String, String> metaEntries) {
		
		
		if(date.equals(""))
			return;
		
		Calendar c = Calendar.getInstance();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			c.setTime(df.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long snapMilis = c.getTimeInMillis();
		if(firstSnapMilis==0){
			firstSnapMilis = snapMilis;
			firstSnapDate = date;
			
			DateFormat dfM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cm = Calendar.getInstance();
			try {
				cm.setTime(dfM.parse(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			referenceDate = cm.getTimeInMillis();
		}
		
		
		if(snapMilis-firstSnapMilis>60000){
			indexSnapShot(date, densitySum/snapsCount,accumulatedMap); // take the average and the first occurence as the date.
			firstSnapMilis = snapMilis;
			firstSnapDate = date;
			densitySum=density;
			snapsCount=1;
			accumulatedMap = new HashMap<String, String>();
		}else{
			densitySum+= density;
			snapsCount++;
			
			
			// we will concatenate the metadata ..
			for(String k : metaEntries.keySet()){
				if(accumulatedMap.containsKey(k)){
					String v = accumulatedMap.get(k);
					if(!v.equals(metaEntries.get(k)))
						v = v + " " + metaEntries.get(k);
					accumulatedMap.put(k, v);
				}
				else
					accumulatedMap.put(k, metaEntries.get(k));
			}
		}
		
	}
	
	
	public void accumulateSnapShot(String date, double density, Map<String, String> metaEntries, int location) {
		this.accumulateSnapShot(date, density, metaEntries, location, null);
		
		
	}
	
	
	// ---
	public void accumulateSnapShot(String date, double density, Map<String, String> metaEntries, int location, double[] colors){
		
		if(date.equals(""))
			return;
		
		Calendar c = Calendar.getInstance();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			c.setTime(df.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long snapMilis = c.getTimeInMillis();
		if(firstSnapsMilis[location]==0){
			firstSnapsMilis[location] = snapMilis;
			firstSnapsDate[location] = date;
			
			DateFormat dfM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cm = Calendar.getInstance();
			try {
				cm.setTime(dfM.parse(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(referenceDate==0)
				referenceDate = cm.getTimeInMillis();
		}
		
		
		if(snapMilis-firstSnapsMilis[location]>60000){
			indexSnapShot(date, densitySums[location]/snapsCounts[location],(HashMap<String,String>)accumulatedMaps[location],location,colors); // take the average and the first occurence as the date.
			firstSnapsMilis[location] = snapMilis;
			firstSnapsDate[location] = date;
			densitySums[location]=density;
			snapsCounts[location]=1;
			accumulatedMaps[location] = new HashMap<String, String>();
		}else{
			densitySums[location]+= density;
			snapsCounts[location]++;
			
			
			// we will concatenate the metadata ..
			for(String k : metaEntries.keySet()){
				HashMap<String,String> map = (HashMap<String, String>)accumulatedMaps[location];
				
				if(map.containsKey(k)){
					String v = map.get(k);
					if(!v.equals(metaEntries.get(k)))
						v = v + " " + metaEntries.get(k);
					map.put(k, v);
				}
				else
					map.put(k, metaEntries.get(k));
			}
			
		}
	}
	
	public void indexSnapShot(String date, double density,Map<String, String> metaEntries, int location, double[] colors) {

		double coefficient = density / crowdSampleUnit;

		int tfCrowd = (int) coefficient;

		int doclength = 0;
		int docid = 0;

		//docid = memcollstats.getNumberOfDocuments();
		// Process terms through term pipeline.
		
		
		DateFormat dfM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cm = Calendar.getInstance();
		try {
			cm.setTime(dfM.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		docid =(int) (cm.getTimeInMillis()-referenceDate)/60000 + location * BLOCK_SIZE;
		
		
		
		DocumentPostingList docPostings = new DocumentPostingList();
		
		String term = tpa.pipelineTerm(CROWD_TOKEN);		

		for (int i = 0; i < tfCrowd; i++)
			docPostings.insert(term);
		
		// Add/update term in lexicon.
		memlex.term(CROWD_TOKEN, new MemoryLexiconEntry(1,
						docPostings.getFrequency(CROWD_TOKEN)));
		
		if(colors!=null)
			for(double c: colors){
				int color = (int)c;
				String colorname = ColorConversionUtility.findClosestColor(color);
				colorname = colorname.toLowerCase();
				for(String t: colorname.split(" ")){
					tpa.pipelineTerm(t);
					docPostings.insert(t);
					memlex.term(t, new MemoryLexiconEntry(1,
							docPostings.getFrequency(t)));
				}
			}
		
		

		doclength = docPostings.getDocumentLength();

		// Update collection statistics.
		memcollstats.update(1, doclength, docPostings.termSet().length);
		memcollstats.updateUniqueTerms(memlex.numberOfEntries());

		Map<String, String> metaIndexEntries = metaEntries;
		
		metaIndexEntries.put(TIMESTAMP_KEY, date);
		
		// added 3-12-2012
		metaIndexEntries.put("density", density+"");		
		
		try {
			metadata.writeDocumentEntry(docid, metaIndexEntries);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

		document.addDocument(docid, docPostings.getDocumentLength());
		for(String t:docPostings.termSet()){
			int termId= memlex.getLexiconEntry(t).getTermId();
			inverted.add(termId, docid, docPostings.getFrequency(t));
		}
		
		
	}

	public void indexSnapShot(String date,double density, Map<String,String> metaEntries, int location){
		this.indexSnapShot(date, density, metaEntries, location, null);
	}
	
	
	
	public void indexSnapShot(String date,double density, Map<String,String> metaEntries){

		double coefficient = density / crowdSampleUnit;

		int tfCrowd = (int) coefficient;

		int doclength = 0;
		int docid = 0;

		//docid = memcollstats.getNumberOfDocuments();
		// Process terms through term pipeline.
		
		
		DateFormat dfM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cm = Calendar.getInstance();
		try {
			cm.setTime(dfM.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		docid =(int) (cm.getTimeInMillis()-referenceDate)/60000;
		
		
		DocumentPostingList docPostings = new DocumentPostingList();
		
		String term = tpa.pipelineTerm(CROWD_TOKEN);		

		for (int i = 0; i < tfCrowd; i++)
			docPostings.insert(term);

		// Add/update term in lexicon.
		int termId = memlex.term(CROWD_TOKEN, new MemoryLexiconEntry(1,
				docPostings.getFrequency(CROWD_TOKEN)));

		doclength = docPostings.getDocumentLength();

		// Update collection statistics.
		memcollstats.update(1, doclength, docPostings.termSet().length);
		memcollstats.updateUniqueTerms(memlex.numberOfEntries());

		Map<String, String> metaIndexEntries = metaEntries;
		
		metaIndexEntries.put(TIMESTAMP_KEY, date);
		try {
			metadata.writeDocumentEntry(docid, metaIndexEntries);
		} catch (Exception e) {
			e.printStackTrace();
		}

		document.addDocument(docid, docPostings.getDocumentLength());
		inverted.add(termId, docid, docPostings.getFrequency(CROWD_TOKEN));
		
	}
	

	public void indexSnapShot(ValueRow<Object> v) {
		
		String date = (String)v.getKey();
		Double density = Double.parseDouble(v.getValue().toString());

		double coefficient = density / crowdSampleUnit;

		int tfCrowd = (int) coefficient;

		int doclength = 0;
		int docid = 0;

		docid = memcollstats.getNumberOfDocuments();
		// Process terms through term pipeline.
		DocumentPostingList docPostings = new DocumentPostingList();
		
		String term = tpa.pipelineTerm(CROWD_TOKEN);		

		for (int i = 0; i < tfCrowd; i++)
			docPostings.insert(term);

		// Add/update term in lexicon.
		int termId = memlex.term(CROWD_TOKEN, new MemoryLexiconEntry(1,
				docPostings.getFrequency(CROWD_TOKEN)));

		doclength = docPostings.getDocumentLength();

		// Update collection statistics.
		memcollstats.update(1, doclength, docPostings.termSet().length);
		memcollstats.updateUniqueTerms(memlex.numberOfEntries());

		Map<String, String> metaIndexEntries = new HashMap<String, String>();
		metaIndexEntries.put(TIMESTAMP_KEY, date);
		try {
			metadata.writeDocumentEntry(docid, metaIndexEntries);
		} catch (Exception e) {
			e.printStackTrace();
		}

		document.addDocument(docid, docPostings.getDocumentLength());
		inverted.add(termId, docid, docPostings.getFrequency(CROWD_TOKEN));
	}
	
	
	
	
	public MemoryMetaIndexMap getMetadata() {
		return metadata;
	}

	public MemoryLexicon getMemlex() {
		return memlex;
	}

	public static void main(String[] args){
		/*
		System.setProperty("terrier.home", System.getProperty("user.dir"));		
		System.setProperty("matching.dsms", "ChangePointScoreModifier");
		System.setProperty("ignore.low.idf.terms","false");
		System.setProperty("indexer.meta.forward.keys",SensorSearch.TIMESTAMP_KEY);
		System.setProperty("indexer.meta.forward.keylens","30");
		
		System.setProperty("matching.retrieved_set_size","0");
		
		SensorSearch sensorSearch = new SensorSearch();
		sensorSearch.batchIndex();
		ResultSet rs = sensorSearch.search(SensorSearch.CROWD_TOKEN);
		
		int i=0;
		List<Integer> results = new ArrayList<Integer>(); 
		
		for(int id:rs.getDocids()){
			
			
			
			try {
				double s= rs.getScores()[i++];
				if(s>-100.00){
					System.out.print(id +"\t");
					System.out.print(sensorSearch.getMetadata().getItem(SensorSearch.TIMESTAMP_KEY, id)+"\t"+
							s);
					results.add(id);
					System.out.println();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		Evaluation ev = new Evaluation("share/qrels", sensorSearch.getReferenceDate());
		
		System.out.println(ev.performEvaluation(results));
		
		ev.calculateTransitions(results, 5823);
		
		System.out.println("at cut 5");
		ev.calculateTransitions(results.subList(0, 5), 5823);
		
		
		System.out.println("at cut 10");
		ev.calculateTransitions(results.subList(0, 10), 5823);
		
		System.out.println("at cut 20");
		ev.calculateTransitions(results.subList(0, 20), 5823);
		*/
		
	}
	
}
