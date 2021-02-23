package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIBankServer extends Remote{
	
	int createAccountRMI() throws RemoteException;
	
	String depositRMI(int uid, int amount) throws RemoteException;
	
	int getBalanceRMI(int uid) throws RemoteException;
	
	String transferRMI(int sourceId, int targetId, int amount) throws RemoteException;
	
}
