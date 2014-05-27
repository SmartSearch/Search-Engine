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


package eu.smartfp7.jcouchdb;

import org.svenson.JSON;
import org.svenson.JSONProperty;

public class SmartChangeEntry {

	private String rev;

    public String getRev()
    {
        return rev;
    }

    public void setRev(String rev)
    {
        this.rev = rev;
    }
    
    
    
    

    @Override
    public String toString()
    {
        return super.toString() + "[rev=" + rev + "]";
    }
	
}
