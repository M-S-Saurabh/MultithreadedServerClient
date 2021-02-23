package rmi;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import tcp.shared.Constants;

public class RMIClientThread implements Runnable {
	
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private RMIBankServer bankServer;
	private List<Integer> accountIds;
	private int iterationCount;
	private Random random;

	public RMIClientThread(RMIBankServer bankServer, List<Integer> accountIds, int iterationCount) {
		this.bankServer = bankServer;
		this.accountIds = accountIds;
		this.iterationCount = iterationCount;
		this.random = new Random();
	}

	@Override
	public void run() {
		for(int i=0; i<iterationCount; i++) {
			// Pick a random element from list.
			int first = accountIds.get(random.nextInt(accountIds.size()));
			
			// Pick another random element from list until you get a different element.
			int second = accountIds.get(random.nextInt(accountIds.size()));
			while(second == first) {
				second = accountIds.get(random.nextInt(accountIds.size()));
			}
			
			try {
				// Transfer 10$ from first account to second account
				String status = bankServer.transferRMI(first, second, 10);
				
				// log the transaction status.
				if(status.equals(Constants.FAIL_STATUS)) {
					logger.severe(String.format("Transfer request failed. SourceID:%d TargetID:%d", first, second));
					logger.severe(String.format("Failure Reason: %s", Constants.INSUFFICIENT_BALANCE));
				}else {
					logger.fine(String.format("Transfer of 10$ from id:%d to id:%d successful.", first, second));
				}
				
			} catch (RemoteException e) {
				logger.severe("Couldn't invoke rmi transfer method.");
				logger.severe(e.getMessage());
				e.printStackTrace();
			}
		}

	}

}
