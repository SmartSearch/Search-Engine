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


package eu.smartfp7.terrier.storm.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.Responses;

public class InvalidRequestException extends WebApplicationException {
	private static final long serialVersionUID = 1L;

	public InvalidRequestException()
	{
		super(Response.status(Responses.CLIENT_ERROR).build());
	}
	
    public InvalidRequestException(String message) {
    	super(Response.status(Responses.CLIENT_ERROR).
    	entity(message).type("text/plain").build());
    }
	
}
