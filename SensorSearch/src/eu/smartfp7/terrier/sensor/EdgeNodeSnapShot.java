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

import java.util.Calendar;

/**
 * CameraSnapShot contains analysis data from a camera Node
 *  	at a certain time
 *   
 * 
 * @author dyaa
 *
 */
public class EdgeNodeSnapShot implements Comparable<EdgeNodeSnapShot>{

	private Node node;
	
	private String id;
	
	private Calendar timestamp;
		
	private CrowdReport crowdData;

	/**
	 * Some text describing an activity detected by the edge node
	 */
	private String text;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public EdgeNodeSnapShot(Node node, String id, Calendar timestamp,
			CrowdReport crowdData) {
		super();
		this.node = node;
		this.id = id;
		this.timestamp = timestamp;
		this.crowdData = crowdData;
	}

	public EdgeNodeSnapShot() {
		super();
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public CrowdReport getCrowdData() {
		return crowdData;
	}

	public void setCrowdData(CrowdReport crowdData) {
		this.crowdData = crowdData;
	}

	@Override
	public int compareTo(EdgeNodeSnapShot o) {		
		return this.getTimestamp().compareTo(o.getTimestamp());
	}
	
	
	
	
	
	
	
}
