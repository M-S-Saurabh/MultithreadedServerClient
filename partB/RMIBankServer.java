/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partB;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIBankServer extends Remote {
	RMIBankSession login() throws RemoteException;
}
