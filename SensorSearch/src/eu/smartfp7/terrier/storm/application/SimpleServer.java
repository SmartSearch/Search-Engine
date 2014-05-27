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


package eu.smartfp7.terrier.storm.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

	int port;
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	Thread listenThread = null;

	public SimpleServer(int port) {
		this.port = port;
	}

	public void openSocket() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: "+port);
			System.exit(1);
		}

	}

	class ListenAndPrintThread implements Runnable {

		Thread runner;

		public ListenAndPrintThread() {
			runner = new Thread(this, "ListenAndPrintThread"); // (1) Create a
																// new thread.
			System.out.println(runner.getName());			
			
			runner.start(); // (2) Start the thread.
			
		}

		public void run() {
			while (true) {

				try {
					clientSocket = serverSocket.accept();
				} catch (IOException e) {
					System.err.println("Accept failed.");
					System.exit(1);
				}

				try {
					// PrintWriter out = new
					// PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));
					String inputLine = in.readLine();

					while (inputLine!=null) {
						System.out.println(inputLine);
						inputLine = in.readLine();
					}
					in.close();
					clientSocket.close();
					
					/*
					 * out.close(); in.close(); clientSocket.close();
					 * serverSocket.close();
					 */
				} catch (Exception e) {
					if (clientSocket != null) {
						try {
							clientSocket.close();
							serverSocket.close();
						} catch (IOException e1) {
						}
					}
					openSocket();
					e.printStackTrace();
				}
			}
		}
	}

	public void beginListening() {
		listenThread = new Thread(new ListenAndPrintThread(), "ListenThread1");
	}

}
