/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partA.server;

import java.net.*;
import java.util.Hashtable;
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
        FileHandler fh = new FileHandler("./logs/TCP_serverLogfile.log");  
        logger.addHandler(fh);
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter); 

		logger.info("Starting on port " + args[0]);
		ServerSocket server = new ServerSocket (Integer.parseInt (args[0]));

		// Maintain a set of all accounts. This is where all the acccounts data is stored.
		Hashtable<Integer, BankAccount> accounts = new Hashtable<Integer, BankAccount>(100);

		while (true) {
			logger.info("Waiting for a partA.client request");
			Socket client = server.accept();
			logger.info( "Received request from " + client.getInetAddress ());
			logger.info( "Starting worker thread..." );
			
			// Create a separate server thread to handle every new client connection.
			TCPServerThread c = new TCPServerThread(client, accounts);
			new Thread(c).start();
		}
	}

}
