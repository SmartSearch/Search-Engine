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


package eu.smartfp7.terrier.sensor.terrier;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.terrier.matching.Matching;
import org.terrier.matching.MatchingQueryTerms;
import org.terrier.matching.ResultSet;
import org.terrier.matching.models.BM25;
import org.terrier.matching.models.DLH13;
import org.terrier.structures.Index;

import eu.smartfp7.terrier.sensor.ColorConversionUtility;
import eu.smartfp7.terrier.sensor.EdgeNodeSnapShot;
import eu.smartfp7.terrier.sensor.ParserUtility;

public class IndexCrowdReports {

	public static void main(String[] args) throws FileNotFoundException, Exception{
				
		// parse the snap shots
		String pathToFolder = "share/rdf";
		
		List<EdgeNodeSnapShot> snapShots = new ArrayList<EdgeNodeSnapShot>();
				
		File folder = new File(pathToFolder);		
		for(File f : folder.listFiles()){
			if(f.isFile()){
				snapShots.add(ParserUtility.parse(new FileInputStream(f)));				
			}	
		}		
		
		// index them..		
		String[] docids = new String[snapShots.size()];
		String[] documents = new String[snapShots.size()];
		
		for(int i=0;i<snapShots.size();i++){
			
			String[] transformed = transformCameraSnapShot(snapShots.get(i));
			docids[i]=transformed[0];
			documents[i]=transformed[1];
			
		}
		//Index index = IndexTestUtils.makeIndex(docids,documents);
		
		// query them	
		/*Matching matching = new org.terrier.matching.daat.FatFull(index);
		
		MatchingQueryTerms mqt = new MatchingQueryTerms();
		mqt.setTermProperty("crowd", 10);
		mqt.setDefaultTermWeightingModel(new BM25());		
		
		ResultSet rs = matching.match("query1", mqt);
		System.out.println(rs.getResultSize());
		
		for(int i=0;i<rs.getResultSize();i++){
			System.out.println(rs.getDocids()[i]+"\t"+ rs.getScores()[i]);
		}*/		
	}
	
	// transform a camera snap shot to a document ( a pair of doc id and tokens ).
	private static String[] transformCameraSnapShot(EdgeNodeSnapShot snapShot){
		
		
		long timeInMilis= snapShot.getTimestamp().getTimeInMillis();
		String documentId = timeInMilis+"";
		
		StringBuilder content = new StringBuilder();
		
		for(double c: snapShot.getCrowdData().getColors()){
			int color = (int)c;
			String colorname = ColorConversionUtility.findClosestColor(color);
			content.append(colorname+"\t");
		};
		content.append("\n");
		
		double coefficient= snapShot.getCrowdData().getDensity()/0.04;
		for(int i=0;i<(int)coefficient;i++){
			content.append("$CROWD"+"\t");
		}		
		return new String[]{documentId,content.toString()};
	}
	
}
