/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partB;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIBankSession extends Remote {
	
	int createAccountRMI() throws RemoteException;
	
	String depositRMI(int uid, int amount) throws RemoteException;
	
	int getBalanceRMI(int uid) throws RemoteException;
	
	String transferRMI(int sourceId, int targetId, int amount) throws RemoteException;
	
	 void logout() throws RemoteException;
}
