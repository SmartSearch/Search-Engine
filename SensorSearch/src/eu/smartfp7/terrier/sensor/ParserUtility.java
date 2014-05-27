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

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParserUtility {

	
	public static EdgeNodeSnapShot parse(InputStream is)
		throws Exception{
		DocumentBuilderFactory xmlfact = DocumentBuilderFactory.newInstance();
		xmlfact.setNamespaceAware(true);
		Document document = xmlfact.newDocumentBuilder().parse(is);
		
		
		NamespaceContext ctx = new NamespaceContext() {
			
			@Override
			public Iterator getPrefixes(String namespaceURI) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPrefix(String namespaceURI) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getNamespaceURI(String prefix) {
				
				String uri=null;
				
				/*
				 * 	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
xmlns:smart="http://www.ait.gr/ait_web_site/faculty/apne/schema.xml#"
xmlns:dc="http://purl.org/dc/elements/1.1/"
xmlns:contact="http://www.w3.org/2000/10/swap/pim/contact#"
xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#"
xmlns:time="http://www.w3.org/2006/time#"
xml:base="http://www.ait.gr/ait_web_site/faculty/apne/report.xml"

*
*/
				
				if(prefix.equals("rdf")){
					uri="http://www.w3.org/1999/02/22-rdf-syntax-ns#";
				}
				if(prefix.equals("smart")){
					uri="http://www.ait.gr/ait_web_site/faculty/apne/schema.xml#";
				}
				if(prefix.equals("dc")){
					uri="http://purl.org/dc/elements/1.1/#";
				}
				if(prefix.equals("geo")){
					uri="http://www.w3.org/2003/01/geo/wgs84_pos#";
				}
				if(prefix.equals("time")){
					uri="http://www.w3.org/2006/time#";
				}
				return uri;
				
			}
		};
		
		// find the node data
		
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(ctx);
		
		
						
		String id  = (String)xpath.compile("//smart:Node/@rdf:ID").evaluate(document, XPathConstants.STRING);				
		String lat= (String) xpath.compile("//smart:Node/geo:lat/text()").evaluate(document, XPathConstants.STRING);
		String lon  = (String) xpath.compile("//smart:Node/geo:long/text()").evaluate(document, XPathConstants.STRING);				
		String fullName  = (String)xpath.compile("//smart:Node/dc:fullName/text()").evaluate(document, XPathConstants.STRING);				
		Node node = new Node(new Double(lat), new Double(lon), id, fullName);
		
		
		String time  = (String)xpath.compile("//smart:Report/time:inXSDDateTime/text()").evaluate(document, XPathConstants.STRING);				
		String reportId= (String) xpath.compile("//smart:Report/@rdf:ID").evaluate(document, XPathConstants.STRING);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	
		Calendar c = Calendar.getInstance();
		;
		c.setTime(df.parse(time));
		
		
		String density  = (String)xpath.compile("//smart:Crowd/smart:density/text()").evaluate(document, XPathConstants.STRING);
		String cameraGain  = (String)xpath.compile("//smart:Crowd/smart:cameraGain/text()").evaluate(document, XPathConstants.STRING);
		
		
		NodeList list = (NodeList)xpath.compile("//smart:Crowd/smart:colour").evaluate(document, XPathConstants.NODESET);
		double[] colors = new double[list.getLength()];
		for(int i=0;i<list.getLength();i++){
			org.w3c.dom.Node colorNode = list.item(i);
			String v=colorNode.getFirstChild().getTextContent();
			colors[i]= new Double(v);
		}
		
		
		CrowdReport crowdReport = new CrowdReport(reportId, new Double(density), new Double(cameraGain), colors);
		
		EdgeNodeSnapShot snapShot = new EdgeNodeSnapShot(node, id, c, crowdReport);
		
		return snapShot;
	}
	
	
	public static EdgeNodeSnapShot parseShort(InputStream is)
			throws Exception{
		
		DocumentBuilderFactory xmlfact = DocumentBuilderFactory.newInstance();
		xmlfact.setNamespaceAware(true);
		Document document = xmlfact.newDocumentBuilder().parse(is);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		
		
		String time  = (String)xpath.compile("//crowd/time/text()").evaluate(document, XPathConstants.STRING);	
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	
		Calendar c = Calendar.getInstance();
		;
		c.setTime(df.parse(time));
		
		String density  = (String)xpath.compile("//crowd/density/text()").evaluate(document, XPathConstants.STRING);
		
		NodeList list = (NodeList)xpath.compile("//crowd/colour").evaluate(document, XPathConstants.NODESET);
		double[] colors = new double[list.getLength()];
		for(int i=0;i<list.getLength();i++){
			org.w3c.dom.Node colorNode = list.item(i);
			String v=colorNode.getFirstChild().getTextContent();
			colors[i]= new Double(v);
		}
		
		String activity  = (String)xpath.compile("//activity/name/text()").evaluate(document, XPathConstants.STRING);
		
		CrowdReport crowdReport = new CrowdReport(null, new Double(density), 0.0, colors);
		
		EdgeNodeSnapShot snapShot = new EdgeNodeSnapShot(null, null, c, crowdReport);
		snapShot.setText((activity!=null?activity:StringUtils.EMPTY));
		
		return snapShot;
	
	}
	
	
	
	public static EdgeNodeSnapShot parseJSON(String json) throws Exception{
		/*
		 * {"activity":{"name":"#lab_visits","isActive":"false","date":"26-07-2012","temporalHint":["07:00","08:00"]},"crowd":{"time":"2012-07-26T07:25:50.650Z","density":"0.000560","motion_horizontal":"-1","motion_vertical":"-1",
		 * "motion_spread":"-1.000000","color":["6316128.087100","2097152.068100","4219008.049900",
		 * "14729408.046900","2105376.042900","8256.041800","2113632.037800","0.037500","4210752.033700","10526944.032100",
		 * "10535136.029100","8429792.028800","8224.027900","2105408.027500","4210784.026400","4202592.022200"]}}},
		 * 
		 * 
		*/
		JsonElement jelement = new JsonParser().parse(json);

		JsonObject  jobject = jelement.getAsJsonObject().getAsJsonObject("crowd");

		String time  = (String)jobject.get("time").getAsString();	
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	
		Calendar c = Calendar.getInstance();
		;
		c.setTime(df.parse(time));
		
		String density  = (String)jobject.get("density").toString();
		
		double[] colors=null;
		if(jobject.has("color")){
			JsonArray jarray = jobject.getAsJsonArray("color");
			
			
			colors = new double[jarray.size()];
			for(int i=0;i<jarray.size();i++){
				String v=jarray.get(0).getAsString();
				colors[i]= new Double(v);
			}
		
		}
		
		String activity=null;
		if(jelement.getAsJsonObject().has("activity")){
			activity=jelement.getAsJsonObject().getAsJsonObject("activity").get("name").getAsString();
		}
		
		CrowdReport crowdReport = new CrowdReport(null, new Double(density), 0.0, colors);
		EdgeNodeSnapShot snapShot = new EdgeNodeSnapShot(null, null, c, crowdReport);
		snapShot.setText((activity!=null?activity:StringUtils.EMPTY));
		
		
		return snapShot;
		
		
		
	}
	
	public static EdgeNodeSnapShot parseHashMap(HashMap hashMap) throws Exception{
		
		/*
		 * ((HashMap)((java.util.HashMap)v.getValue()).get("crowd")).get("time");

	 (java.lang.String) 2012-07-26T06:29:51.947Z
	 (java.lang.String) #lab_visits	 (java.util.ArrayList<E>) [07:00, 08:00]


	 (java.util.HashMap<K,V>) {motion_spread=-1.000000, time=2012-07-26T06:29:51.947Z, motion_horizontal=-1, density=0.000000, motion_vertical=-1}
	 (java.util.HashMap<K,V>) {isActive=false, temporalHint=[07:00, 08:00], name=#lab_visits, date=26-07-2012}
	 (java.util.HashMap<K,V>) {motion_spread=-1.000000, time=2012-07-26T06:29:51.947Z, motion_horizontal=-1, density=0.000000, motion_vertical=-1}
	Evaluation failed. Reason(s):
		ClassCastException: Cannot cast java.util.HashMap (id=185) to java.util.Hashtable
		 * */
		
		HashMap crowdHashMap = (HashMap) hashMap.get("crowd");
		double density= (Double) crowdHashMap.get("density");
		
		String time  = (String)crowdHashMap.get("time");	
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	
		Calendar c = Calendar.getInstance();
		;
		c.setTime(df.parse(time));
		
		double[] colors=null;
		
		if(crowdHashMap.containsKey("color"))
		{
			ArrayList colorList = (ArrayList)crowdHashMap.get("color");
			colors = new double[colorList.size()];
			for(int i=0;i<colorList.size();i++){
				colors[i] = (Double)colorList.get(i);
			}
		}
			
		
		
		
		
		CrowdReport crowdReport = new CrowdReport(null, new Double(density), 0.0, colors);
		EdgeNodeSnapShot snapShot = new EdgeNodeSnapShot(null, null, c, crowdReport);
		
		
		return snapShot;
	}
}
