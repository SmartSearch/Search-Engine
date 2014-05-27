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

/**
 * CrowdReport contains the crowd analysis results
 *  performed on SMART edges.
 * 
 * 
 * @author dyaa
 *
 */
public class CrowdReport {

	private String id;
	
	private double density;
	
	private double cameraGain;
	
	private double[] colors;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	public double getCameraGain() {
		return cameraGain;
	}

	public void setCameraGain(double cameraGain) {
		this.cameraGain = cameraGain;
	}

	public double[] getColors() {
		return colors;
	}

	public void setColors(double[] colors) {
		this.colors = colors;
	}

	public CrowdReport(String id, double density, double cameraGain,
			double[] colors) {
		super();
		this.id = id;
		this.density = density;
		this.cameraGain = cameraGain;
		this.colors = colors;
	}

	public CrowdReport() {
		super();
	}

	
	
	
}
