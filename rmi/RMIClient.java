package rmi;


import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import rmi.Constants;

public class RMIClient {
	
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static List<Integer> accountIds;

	public static void main(String[] args) throws SecurityException, IOException{
		if (args.length != 4)
			throw new RuntimeException ("Syntax: RMIClient <hostname> <port> <threadCount> <iterationCount>");
		
		// This block configure the logger with handler and formatter  
        FileHandler fh = new FileHandler("./logs/RMIClientLog.log");  
        logger.addHandler(fh);
        System.setProperty("java.util.logging.SimpleFormatter.format", Constants.LOG_FORMAT);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);
        
        accountIds = new ArrayList<Integer>();
        
        System.setProperty("java.security.policy","file:./security.policy");
		
        System.setSecurityManager(new SecurityManager());
		try {
			RMIBankServer bankServer = (RMIBankServer) Naming.lookup(
					String.format("rmi://%s:%s/%s", args[0], args[1], Constants.RMI_SERVER_NAME)
			);
			
			createAccounts(bankServer, 100);
			
			depositAllAccounts(bankServer, 100);
			
			checkBalances(bankServer);
			
			spawnThreads(bankServer, Integer.parseInt(args[2]), Integer.parseInt(args[3]));
			
			checkBalances(bankServer);
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			logger.severe("Couldn't establish RMI registry connection.");
			logger.severe(e.getMessage());
			e.printStackTrace();
		}

	}

	
	private static void spawnThreads(RMIBankServer bank, int threadCount, int iterationCount) throws RemoteException { 
		List<AbstractMap.SimpleEntry<Thread, RMIBankSession>> transferThreads = new LinkedList<>();
		for (int i=0; i<threadCount; i++) {
			RMIBankSession session = bank.login();
			RMIClientThread c = new RMIClientThread(session, accountIds, iterationCount); 
			Thread txThread = new Thread(c);
			txThread.start();
			transferThreads.add(new AbstractMap.SimpleEntry<Thread, RMIBankSession>(txThread, session)); 
		}
		
		for (AbstractMap.SimpleEntry<Thread, RMIBankSession> entry: transferThreads) {
			try {
				entry.getKey().join();
				entry.getValue().logout();
			} catch(InterruptedException e) { 
				logger.severe("join failed"); e.printStackTrace();
			}
	    }
	 }
	

	private static void checkBalances(RMIBankServer bank) throws RemoteException {
		StringBuilder sbR = new StringBuilder("Account-wise balance: ");
		RMIBankSession bankServer = bank.login();
		int totalBalance = 0;
		for(int accountId: accountIds) {
			int balance = bankServer.getBalanceRMI(accountId);
			totalBalance += balance;
			
			sbR.append(String.format("%d:%d, ", accountId, balance));
		}
		bankServer.logout();
		logger.info(sbR.toString());
		logger.severe(String.format("Total balance (sum): %d", totalBalance));
	}

	private static void depositAllAccounts(RMIBankServer bank, int amount) throws RemoteException {
		RMIBankSession bankServer = bank.login();
		for(int accountId: accountIds) {
			bankServer.depositRMI(accountId, amount);
		}
		bankServer.logout();
		logger.severe("Deposited 100$ in all accounts.");
	}

	private static void createAccounts(RMIBankServer bank, int numAccounts) throws RemoteException {
		RMIBankSession bankServer = bank.login();
		StringBuilder sbR = new StringBuilder("Created account ids: ");
		
		for(int i=0; i<numAccounts; i++) {
			int uid = bankServer.createAccountRMI();
			accountIds.add(uid);
			
			sbR.append(uid);
			sbR.append(", ");
		}
		bankServer.logout();
		logger.severe(sbR.toString());
	}

}
