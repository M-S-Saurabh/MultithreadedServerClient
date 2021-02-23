package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIBankServer extends Remote {
	RMIBankSession login() throws RemoteException;
}