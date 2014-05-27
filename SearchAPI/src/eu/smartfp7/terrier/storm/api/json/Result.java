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


package eu.smartfp7.terrier.storm.api.json;

import java.io.Serializable;
import java.util.Calendar;

public abstract class Result implements Serializable {
	private static final long serialVersionUID = 1L;
	public int rank;
	public double score;
	public String description;
	public String URI;
	public String title;
	
	
	public abstract boolean isBefore(Calendar beforeCal);
	
	public abstract boolean inLocation(int locationId);
	
}
