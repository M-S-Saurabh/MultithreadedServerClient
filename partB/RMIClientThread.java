/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partB;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import partA.shared.Constants;

public class RMIClientThread implements Runnable {
	
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private RMIBankSession session; // one session per client thread
	private List<Integer> accountIds;
	private int iterationCount;
	private Random random;

	public RMIClientThread(RMIBankSession bankServer, List<Integer> accountIds, int iterationCount) {
		this.session = bankServer;
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
				String status = session.transferRMI(first, second, 10);
				
				// log the transaction status.
				if(status.equals(Constants.FAIL_STATUS)) {
					logger.severe(String.format("Transfer request failed. SourceID:%d TargetID:%d", first, second));
					logger.severe(String.format("Failure Reason: %s", Constants.INSUFFICIENT_BALANCE));
				}else {
					logger.info(String.format("Transfer of 10$ from id:%d to id:%d status:%s", first, second, status));
				}
				
			} catch (RemoteException e) {
				logger.severe("Couldn't invoke partB transfer method.");
				logger.severe(e.getMessage());
				e.printStackTrace();
			}
		}

	}

}
