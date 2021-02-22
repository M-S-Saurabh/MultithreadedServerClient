package client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import shared.Constants;
import shared.RMIBankServer;

public class RMIClient {
	
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static List<Integer> accountIds;

	public static void main(String[] args) throws SecurityException, IOException{
		if (args.length != 2)
			throw new RuntimeException ("Syntax: RMIClient <hostname> <port>");
		
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
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			logger.severe("Couldn't establish RMI registry connection.");
			logger.severe(e.getMessage());
			e.printStackTrace();
		}

	}

	private static void checkBalances(RMIBankServer bankServer) throws RemoteException {
		StringBuilder sbR = new StringBuilder("Account-wise balance: ");
		
		int totalBalance = 0;
		for(int accountId: accountIds) {
			int balance = bankServer.getBalanceRMI(accountId);
			totalBalance += balance;
			
			sbR.append(String.format("%d:%d, ", accountId, balance));
		}
		logger.info(sbR.toString());
		logger.severe(String.format("Total balance (sum): %d", totalBalance));
	}

	private static void depositAllAccounts(RMIBankServer bankServer, int amount) throws RemoteException {
		for(int accountId: accountIds) {
			bankServer.depositRMI(accountId, amount);
		}
		logger.severe("Deposited 100$ in all accounts.");
	}

	private static void createAccounts(RMIBankServer bankServer, int numAccounts) throws RemoteException {
		StringBuilder sbR = new StringBuilder("Created account ids: ");
		
		for(int i=0; i<numAccounts; i++) {
			int uid = bankServer.createAccountRMI();
			accountIds.add(uid);
			
			sbR.append(uid);
			sbR.append(", ");
		}
		
		logger.severe(sbR.toString());
	}

}
