/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partB;

import java.rmi.Remote;
import java.rmi.RemoteException;

// session that is returned by the bank server we achieve multithreading by creating an instance of this per client call
public interface RMIBankSession extends Remote {
	
	// helps us create an account in the bank server
	int createAccountRMI() throws RemoteException;
	
	// helps us deposit money in a bank account
	String depositRMI(int uid, int amount) throws RemoteException;
	
	// helps us get the balance of a bank account
	int getBalanceRMI(int uid) throws RemoteException;
	
	// helps us transfer money between bank accounts
	String transferRMI(int sourceId, int targetId, int amount) throws RemoteException;
	
	
	// called at the client end when the client is done sending requests
	// we achieve the closing of an RMI thread by calling logout which unexports the object instance
	 void logout() throws RemoteException;
}
