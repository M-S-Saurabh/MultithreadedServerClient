/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partA.client;

import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import partA.shared.BalanceRequest;
import partA.shared.BalanceResponse;
import partA.shared.Constants;
import partA.shared.CreateAccountRequest;
import partA.shared.CreateAccountResponse;
import partA.shared.DepositRequest;
import partA.shared.DepositResponse;

import java.io.*;

public class TCPClient   {
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private static List<Integer> accountIds;
	protected String host, file;
	protected int port;

	public static void main (String args[]) throws IOException {

		if (args.length != 4) {
			throw new RuntimeException("Correct usage is 'java BankClient serverHostname severPortnumber threadCount iterationCount'" );
		}
		
	    accountIds = new ArrayList<Integer>();
		
		// This block configure the logger with handler and formatter  
        FileHandler fh = new FileHandler("./logs/TCP_clientLogfile.log");  
        logger.addHandler(fh);
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter); 

		String host = args[0];
		int  port = Integer.parseInt( args[1] );
		
		logger.info("Connecting to " + host + ":" + port + "..");

		// This socket is used to communication related to the main thread.
		Socket socket = new Socket (host, port);
		OutputStream out = socket.getOutputStream ();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		
		InputStream in  = socket.getInputStream();
		ObjectInputStream oin = new ObjectInputStream(in);
		logger.info("Connected.");
		
		// Creating 100 accounts.
		createAccounts(100, oin, oos);
		
		// Depositing 100 in each account.
		depositInAccount(oin, oos);
	    
	    // Checking balance for all accounts.
	    checkBalance(oin, oos);
		
		int threadCount = Integer.parseInt(args[2]);
		int iterationCount = Integer.parseInt(args[3]);
		
		// Spawn off threads to do the transfer tasks.
		List<Thread> transferThreads = new LinkedList<>();
		for(int i=0; i<threadCount; i++) {
			// Each new thread has its own socket to handle communication.
			TCPClientThread c = new TCPClientThread(accountIds, iterationCount, new Socket (host, port));
			Thread txThread = new Thread(c);
			txThread.start();
			transferThreads.add(txThread);
		}
		
		// Join (block) until all threads are done.
		// i.e. all transfer operations are completed.
		for (Thread thread: transferThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				logger.severe("join failed");
				e.printStackTrace();
			}
		}
		
		// Check balance again.
		checkBalance(oin, oos);
		
		// Tasks alloted to the main thread end here. 
		socket.close();
	}

	/*
	 * Makes requests to creates the specified number of bank accounts.
	 */
	private static void createAccounts(int numAccounts, ObjectInputStream oin, ObjectOutputStream oos) throws IOException {
		// Stores all responses to log them and indicate any failures.
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
		
		// Parsing results and writing logs.
		StringBuilder sbR = new StringBuilder("Created account ids: ");
		for(CreateAccountResponse response: responseList) {
			int accID = response.getUid();
			sbR.append(accID);
			sbR.append(" ");
			// Take note of IDs of all the created accounts.
			accountIds.add(accID);
		}
		logger.info(sbR.toString());
	}
	
	/*
	 * Makes requests to deposit 100$ in each bank account.
	 */
	private static void depositInAccount(ObjectInputStream oin, ObjectOutputStream oos) throws IOException {
	
		List<DepositResponse> responseList = new ArrayList<DepositResponse>();
		
		for (int accID: accountIds) {
			oos.writeObject(new DepositRequest(accID, 100));
			try {
				responseList.add((DepositResponse) oin.readObject());
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		// Parsing results and writing logs.
		StringBuilder sbD = new StringBuilder("Deposit successful for: ");
	    for (DepositResponse response: responseList) {
	    	if (response.getStatus().equals(Constants.OK_STATUS)) {
	    		sbD.append(response.getUid());
	    		sbD.append(" ");
	    	}
	    }
	    logger.info(sbD.toString());
	}
	
	/*
	 * Makes requests to check balance of each account and also print a totalBalance sum.
	 */
	private static void checkBalance(ObjectInputStream oin, ObjectOutputStream oos) throws IOException {
		List<BalanceResponse> responseList = new ArrayList<BalanceResponse>();
		
		for (int accID: accountIds) {
			oos.writeObject(new BalanceRequest(accID));
			try {
				responseList.add((BalanceResponse) oin.readObject());
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		// Parse results and calculate sum of all balances.
		// Write results into logs.
		StringBuilder sbR = new StringBuilder("Checking balance. Account-wise balance: ");
	    int total = 0;
	    for (BalanceResponse response: responseList) {
	    	int balance = response.getBalance();
	    	total += balance;
	    	
	    	sbR.append(String.format("%d:%d, ", response.getUid(), balance));
	    }
	    logger.info(sbR.toString());
	    logger.info("Total balance is: "+total);
	}
}
