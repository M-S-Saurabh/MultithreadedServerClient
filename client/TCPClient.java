package client;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import shared.Constants;
import shared.BalanceRequest;
import shared.BalanceResponse;
import shared.CreateAccountRequest;
import shared.CreateAccountResponse;
import shared.DepositRequest;
import shared.DepositResponse;

import java.io.*;

public class TCPClient   {
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private static List<Integer> accountIDS;
	protected String host, file;
	protected int port;

	public static void main (String args[]) throws IOException {

		if (args.length != 2) {
			throw new RuntimeException("hostname and port number as arguments" );
		}
		
	    accountIDS = new ArrayList<Integer>();
		
		// This block configure the logger with handler and formatter  
        FileHandler fh = new FileHandler("./ClientLogFile.log");  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter); 

		String host = args[0];
		int  port = Integer.parseInt( args[1] );
		
		logger.info("Connecting to " + host + ":" + port + "..");

		Socket socket = new Socket (host, port);
		OutputStream out = socket.getOutputStream ();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		
		InputStream in  = socket.getInputStream();
		ObjectInputStream oin = new ObjectInputStream(in);
		logger.info("Connected.");
		
		// Create 100 accounts.
		List<CreateAccountResponse> responseListCreate = createAccounts(100, oin, oos);
		
		StringBuilder sbR = new StringBuilder("Created account ids: ");
		for(CreateAccountResponse response: responseListCreate) {
			int accID = response.getUid();
			sbR.append(accID);
			sbR.append(" ");
			accountIDS.add(accID);
		}
		logger.info(sbR.toString());
		
		List<DepositResponse> responseListDeposit = depositInAccount(oin, oos);
		
		StringBuilder sbD = new StringBuilder("Deposit successful for: ");
	    for (DepositResponse response: responseListDeposit) {
	    	if (response.getStatus().equals(Constants.OK_STATUS)) {
	    		sbD.append(response.getUid());
	    		sbD.append(" ");
	    	}
	    }
	    logger.info(sbD.toString());
	    
	    List<BalanceResponse> responseListBalance = checkBalance(oin, oos);
	    StringBuilder sbB = new StringBuilder("Checking balance:");
	    int total = 0;
	    for (BalanceResponse response: responseListBalance) {
			/*
			 * sbB.append("account: "); sbB.append(response.getUid());
			 * sbB.append(" balance: "); sbB.append(response.getBalance());
			 * sbB.append("\n");
			 */
	    	total += response.getBalance();
	    }
	    logger.info(sbB.toString());
	    logger.info("total balance: "+total);
		socket.close();
	}

	private static List<CreateAccountResponse> createAccounts(int numAccounts, ObjectInputStream oin, ObjectOutputStream oos) throws IOException {
	
		List<CreateAccountResponse> responseList = new ArrayList<CreateAccountResponse>();
		
		for (int i=0; i < numAccounts; i++) {
			oos.writeObject(new CreateAccountRequest());
			try {
				responseList.add((CreateAccountResponse) oin.readObject());
			}
			catch ( ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return responseList;
	}
	
	private static List<DepositResponse> depositInAccount(ObjectInputStream oin, ObjectOutputStream oos) throws IOException {
	
		List<DepositResponse> responseList = new ArrayList<DepositResponse>();
		
		for (int accID: accountIDS) {
			oos.writeObject(new DepositRequest(accID, 100));
			try {
				responseList.add((DepositResponse) oin.readObject());
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return responseList;
	}
	
	private static List<BalanceResponse> checkBalance(ObjectInputStream oin, ObjectOutputStream oos) throws IOException {
		List<BalanceResponse> responseList = new ArrayList<BalanceResponse>();
		
		for (int accID: accountIDS) {
			oos.writeObject(new BalanceRequest(accID));
			try {
				responseList.add((BalanceResponse) oin.readObject());
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return responseList;
	}
}
