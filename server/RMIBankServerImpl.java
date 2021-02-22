package server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import shared.Constants;
import shared.RMIBankServer;

public class RMIBankServerImpl implements RMIBankServer {
	
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	Hashtable<Integer, BankAccount> accounts;
	
	public RMIBankServerImpl() {
		super();
		this.accounts = new Hashtable<Integer, BankAccount>(100);
	}

	@Override
	public int createAccountRMI() throws RemoteException {
		BankAccount newAccount = new BankAccount();
        logger.info("New Account created uid:"+newAccount.UID);
        this.accounts.put(newAccount.UID, newAccount);
		return newAccount.UID;
	}

	@Override
	public String depositRMI(int uid, int amount) throws RemoteException {
		BankAccount account = accounts.get(uid);
		account.setBalance(account.getBalance() + amount);
		return Constants.OK_STATUS;
	}

	@Override
	public int getBalanceRMI(int uid) throws RemoteException {
		BankAccount account = accounts.get(uid);
		if(account == null) {
			logger.severe(String.format("Acccount with id:%d could not be found.", uid));
			return -1;
		}else {
			return account.getBalance();
		}
	}

	@Override
	public String transferRMI(int sourceId, int targetId, int amount) throws RemoteException {
		BankAccount source = accounts.get(sourceId);
		BankAccount target = accounts.get(targetId);
		if(source.getBalance() < amount) {
			logger.severe(String.format("Transfer failed: %s", Constants.INSUFFICIENT_BALANCE));
			return Constants.FAIL_STATUS;
		}else {
			source.setBalance(source.getBalance() - amount);
			target.setBalance(target.getBalance() + amount);
			return Constants.OK_STATUS;
		}
	}

	public static void main(String[] args) throws SecurityException, IOException, AlreadyBoundException {
		if (args.length != 1)
			throw new RuntimeException ("Syntax: RMIBankServerImpl <port>");
		
		// This block configure the logger with handler and formatter  
        FileHandler fh = new FileHandler("./logs/RMIServerLog.log");  
        logger.addHandler(fh);
        System.setProperty("java.util.logging.SimpleFormatter.format", Constants.LOG_FORMAT);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);
        
        System.setProperty("java.security.policy","file:./security.policy");
        
		if(System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		RMIBankServer bankServerStub = (RMIBankServer) UnicastRemoteObject.exportObject(new RMIBankServerImpl(), 0);
		
		int port = Integer.parseInt(args[0]);
		logger.severe(String.format("Using the supplied RMI registry port: %d", port));
		Registry localRegistry = LocateRegistry.getRegistry( port );
		
		localRegistry.bind(Constants.RMI_SERVER_NAME, bankServerStub);

	}

}
