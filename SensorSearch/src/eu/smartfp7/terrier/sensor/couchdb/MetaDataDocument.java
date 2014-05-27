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


package eu.smartfp7.terrier.sensor.couchdb;

import java.util.Collection;
import java.util.Map;

import org.jcouchdb.document.BaseDocument;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

/**
 * 
 * A meta-data document represents a single update from the sensor in the 
 * 	CouchDB.
 * 
 * @author Dyaa Albakour
 *
 */
public class MetaDataDocument extends BaseDocument {

	Map<String, CrowdBean> rdfDocument;
	
	
	
	
	
	
	public Map<String, CrowdBean> getRdfDocument() {
		return rdfDocument;
	}


	@JSONProperty(value="rdf:RDF",ignoreIfNull=true)	
	@JSONTypeHint(CrowdBean.class)
	public void setRdfDocument(Map<String, CrowdBean> rdfDocument) {
		this.rdfDocument = rdfDocument;
	}



	public class CrowdBean{
		double cameraGain;	
		
		Collection<Double> colors;
		
		double density;
		/*
		@JSONProperty(value="smart:cameraGain",ignoreIfNull=true)
		public double getCameraGain() {
			return cameraGain;
		}



		@JSONProperty(value="smart:cameraGain",ignoreIfNull=true)
		public void setCameraGain(double cg){
			this.cameraGain =cg;
		}
		
		
		@JSONProperty(value="smart:color",ignoreIfNull=true)
		public Collection<Double> getColors() {
			return colors;
		}



		@JSONProperty(value="smart:color",ignoreIfNull=true)
		public void setColors(Collection<Double> colors){
			this.colors = colors;
		}
		
		*/
		@JSONProperty(value="smart:density",ignoreIfNull=true)
		public void setDensity(double density){
			this.density = density;
		}


		@JSONProperty(value="smart:density",ignoreIfNull=true)
		public double getDensity() {
			return density;
		}
		
	}
	 
	public class RDFDocument{
		/*
		CrowdDocument crowdDocument;

		ReportDocument reportDocument;
		
		NodeDocument nodeDocument;	
		
		@JSONProperty(value="smart:Crowd")
		public CrowdDocument getCrowdDocument() {
			return crowdDocument;
		}
		
		@JSONTypeHint(CrowdDocument.class)
		@JSONProperty(value="smart:Crowd")
		public void setCrowdDocument(CrowdDocument crowdDocument) {
			this.crowdDocument = crowdDocument;
		}

		@JSONProperty(value="smart:Report")
		public ReportDocument getReportDocument() {
			return reportDocument;
		}
		
		@JSONTypeHint(ReportDocument.class)
		@JSONProperty(value="smart:Report")
		public void setReportDocument(ReportDocument reportDocument) {
			this.reportDocument = reportDocument;
		}

		@JSONProperty(value="smart:Node")
		public NodeDocument getNodeDocument() {
			return nodeDocument;
		}

		@JSONTypeHint(NodeDocument.class)
		@JSONProperty(value="smart:Node")
		public void setNodeDocument(NodeDocument nodeDocument) {
			this.nodeDocument = nodeDocument;
		}
		
		*/
	}


	public class ReportDocument{
		
	}


	public class NodeDocument{
		
		
		
	}



	public class CrowdDocument{
		/*
		double cameraGain;	
		
		Collection<Double> colors;
		
		double density;
		@JSONProperty(value="smart:cameraGain")
		public double getCameraGain() {
			return cameraGain;
		}



		@JSONProperty(value="smart:cameraGain")
		public void setCameraGain(double cg){
			this.cameraGain =cg;
		}
		
		
		@JSONProperty(value="smart:color")
		public Collection<Double> getColors() {
			return colors;
		}



		@JSONProperty(value="smart:color")
		public void setColors(Collection<Double> colors){
			this.colors = colors;
		}
		
		
		@JSONProperty(value="smart:density")
		public void setDensity(double density){
			this.density = density;
		}


		@JSONProperty(value="smart:density")
		public double getDensity() {
			return density;
		}
		
		*/
		
	}

}

