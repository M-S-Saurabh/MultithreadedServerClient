package client;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

import shared.CreateAccountRequest;
import shared.CreateAccountResponse;

import java.io.*;

public class TCPClient   {

	protected String host, file;
	protected int port;

	public static void main (String args[]) throws IOException {
		InetAddress server  = null;
		Socket sock = null;

		if ( args.length != 2 ) {
			throw new RuntimeException( "hostname and port number as arguments" );
		}

		String host = args[0];
		int  port = Integer.parseInt( args[1] );

		System.out.println ("Connecting to " + host + ":" + port + "..");

		Socket socket = new Socket (host, port);
		System.out.println ("Connected.");
		
		// Create 100 accounts.
		List<CreateAccountResponse> responseList = createAccounts(100, socket);
		socket.close();
		
		String resultString = "Created account ids: ";
		for(int i=0; i<responseList.size(); i++) {
			resultString += responseList.get(i).getUid();
			resultString += " ";
		}
		System.out.println(resultString);
	}

	private static List<CreateAccountResponse> createAccounts(int numAccounts, Socket socket) throws IOException {
		OutputStream out = socket.getOutputStream ();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		
		InputStream in  = socket.getInputStream();
		ObjectInputStream oin = new ObjectInputStream(in);
	
		List<CreateAccountResponse> responseList = new ArrayList<CreateAccountResponse>();
		
		for(int i=0; i < numAccounts; i++) {
			oos.writeObject( new CreateAccountRequest());
			try {
				responseList.add((CreateAccountResponse) oin.readObject());
			}
			catch ( ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return responseList;
	}
}
