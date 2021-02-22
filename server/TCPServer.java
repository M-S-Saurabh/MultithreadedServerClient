package server;
/* Author:  Anand Tripathi - Example program code for CSCI 5105 Spring 2021     */

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.*;

public class TCPServer{
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static void main (String args[]) throws IOException {

		if (args.length != 1)
			throw new RuntimeException ("Syntax: EchoServer port-number");
		
		// This block configure the logger with handler and formatter  
        FileHandler fh = new FileHandler("./ServerLogFile.log");  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter); 

		logger.info("Starting on port " + args[0]);
		ServerSocket server = new ServerSocket (Integer.parseInt (args[0]));

		// Accounts list
		List<BankAccount> accounts = Collections.synchronizedList(new ArrayList<BankAccount>());

		while (true) {
			logger.info("Waiting for a client request");
			Socket client = server.accept ();
			logger.info( "Received request from " + client.getInetAddress ());
			logger.info( "Starting worker thread..." );
			TCPServerThread c = new TCPServerThread(client, accounts);
			new Thread(c).start();
		}
	}

}
