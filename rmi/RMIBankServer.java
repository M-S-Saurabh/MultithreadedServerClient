package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Bank Server application that is exposed via the RMI registry
public interface RMIBankServer extends Remote {
	// returns a banksession RMI interface that can be used to get a server thread
	RMIBankSession login() throws RemoteException;
}