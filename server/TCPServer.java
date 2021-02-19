package server;
/* Author:  Anand Tripathi - Example program code for CSCI 5105 Spring 2021     */

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;

public class TCPServer{

	public static void main (String args[]) throws IOException {

		if (args.length != 1)
			throw new RuntimeException ("Syntax: EchoServer port-number");

		System.out.println ("Starting on port " + args[0]);
		ServerSocket server = new ServerSocket (Integer.parseInt (args[0]));

		// Accounts list
		List<BankAccount> accounts = Collections.synchronizedList(new ArrayList<BankAccount>());

		while (true) {
			System.out.println ("Waiting for a client request");
			Socket client = server.accept ();
			System.out.println( "Received request from " + client.getInetAddress ());
			System.out.println( "Starting worker thread..." );
			TCPServerThread c = new TCPServerThread(client, accounts);
			new Thread(c).start();
		}
	}

}
