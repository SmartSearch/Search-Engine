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

public class LocatedResult extends Result implements Serializable {
	private static final long serialVersionUID = 1L;
	Location location;
	Media[] media;
	@Override
	public boolean isBefore(Calendar beforeCal) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean inLocation(int locationId) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
