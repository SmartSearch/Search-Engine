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

import java.util.HashMap;
import java.util.List;

import org.jcouchdb.document.ChangeEntry;
import org.jcouchdb.document.ChangeNotification;
import org.svenson.JSON;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

public class SmartChangeNotification {

	private long sequence;

    private String id;

    private List<SmartChangeEntry> changes;


    public long getSequence()
    {
        return sequence;
    }


    @JSONProperty("seq")
    public void setSequence(long sequence)
    {
        this.sequence = sequence;
    }


    public String getId()
    {
        return id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public List<SmartChangeEntry> getChanges()
    {
        return changes;
    }


    @JSONTypeHint(SmartChangeEntry.class)
    public void setChanges(List<SmartChangeEntry> changes)
    {
        this.changes = changes;
    }


    @Override
    public String toString()
    {
        return super.toString() + "[changes=" + changes + ", id=" + id + ", sequence=" + sequence +
            "]";
    }
    
    private HashMap doc;
    
    public HashMap getDoc(){
    	return doc;
    }
    
    @JSONProperty(value="doc")
    public void setDoc(HashMap doc){
    	this.doc = doc;
    }
    
    
    private boolean deleted;


	public boolean isDeleted() {
		return deleted;
	}

	@JSONProperty("deleted")
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
    
    
    
	
	
}
