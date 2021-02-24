/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partB;

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

public class RMIBankServerImpl extends UnicastRemoteObject implements RMIBankServer {

	private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private Hashtable<Integer, BankAccount> accounts;

	private static int sessionID;


	public RMIBankServerImpl() throws RemoteException {
		super();
		this.sessionID = 0;
		this.accounts = new Hashtable<Integer, BankAccount>(100);
	}

	public static void main(String[] args) throws SecurityException, IOException, AlreadyBoundException {
		if (args.length != 1) {
			throw new RuntimeException("Syntax: RMIBankServerImpl <port>");
		}
	
		  // This block configure the logger with handler and formatter FileHandler fh
		FileHandler fh = new FileHandler("./logs/RMI_serverLogfile.log"); logger.addHandler(fh);
		System.setProperty("java.util.logging.SimpleFormatter.format", Constants.LOG_FORMAT);
		SimpleFormatter formatter = new SimpleFormatter();
	    fh.setFormatter(formatter);
		
	    // setting the security policy
		System.setProperty("java.security.policy", "file:./security.policy");

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		// the stub that is exposed via the RMI registry
		RMIBankServer bankStub = (RMIBankServer) UnicastRemoteObject.toStub(new RMIBankServerImpl());

		int port = Integer.parseInt(args[0]);
		logger.severe(String.format("Using the supplied RMI registry port: %d", port)); 
		Registry localRegistry = LocateRegistry.getRegistry(port);

		localRegistry.bind(Constants.RMI_SERVER_NAME, bankStub);
		// setting up bindings
	}

	@Override
	public RMIBankSession login() throws RemoteException {
		return new RMIBankSessionImpl(this.accounts, sessionID++);
	}
}
