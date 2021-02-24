/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partB;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RMIBankSessionImpl extends UnicastRemoteObject implements RMIBankSession, Unreferenced {
	
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private Hashtable<Integer, BankAccount> accounts;
	private int sessionID;
	
	public RMIBankSessionImpl(Hashtable<Integer, BankAccount> accounts, int sessionID) throws RemoteException {
		super();
		logger.info("s|"+this.sessionID+"|server session created: s"+sessionID);
		this.accounts = accounts;
		this.sessionID = sessionID;
	}

	@Override
	public int createAccountRMI() throws RemoteException {
		BankAccount newAccount = new BankAccount();
        logger.info("s|"+this.sessionID+"|New Account created uid: "+newAccount.UID);
        accounts.put(newAccount.UID, newAccount);
		return newAccount.UID;
	}

	@Override
	public String depositRMI(int uid, int amount) throws RemoteException {
		BankAccount account = accounts.get(uid);
		account.setBalance(account.getBalance() + amount);
		logger.info("s|"+this.sessionID+"|deposite amount: "+amount+" to uid:"+account.UID+ " with status:"+ Constants.OK_STATUS);
		return Constants.OK_STATUS;
	}

	@Override
	public int getBalanceRMI(int uid) throws RemoteException {
		BankAccount account = accounts.get(uid);
		if(account == null) {
			logger.severe(String.format("s|"+this.sessionID+"|Acccount with id:%d could not be found.", uid));
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
			logger.severe(String.format("s|"+this.sessionID+"|Transfer failed: %s", Constants.INSUFFICIENT_BALANCE));
			return Constants.FAIL_STATUS;
		}else {
			source.setBalance(source.getBalance() - amount);
			target.setBalance(target.getBalance() + amount);
			logger.info("s|"+this.sessionID+"|transfer amount: "+amount+" from uid:"+sourceId+ " to uid:"+targetId+ " with status:"+ Constants.OK_STATUS);
			return Constants.OK_STATUS;
		}
	}

	@Override
	public void logout() throws RemoteException {
		// TODO Auto-generated method stub
		UnicastRemoteObject.unexportObject(this, true);
		logger.info("server session closed:"+sessionID);
	}
	
	@Override
	public void unreferenced() {
        try {
        	logger.info("server session closed:"+sessionID);
			UnicastRemoteObject.unexportObject(this, true);
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // needs to be in a try/catch block of course
    }
}
