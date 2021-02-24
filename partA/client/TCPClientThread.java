/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partA.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import partA.shared.Constants;
import partA.shared.TransferRequest;
import partA.shared.TransferResponse;

public class TCPClientThread implements Runnable {
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private int iterationCount;
	private List<Integer> accountIds;
	private Socket socket;
	
	private Random random;

	public TCPClientThread(List<Integer> accountIds, int iterationCount, Socket socket) {
		this.accountIds = accountIds;
		this.iterationCount = iterationCount;
		this.socket = socket;
		random = new Random();
	}

	@Override
	public void run() {
		ObjectOutputStream oos = null;
		ObjectInputStream oin = null;
		try {
			OutputStream out = socket.getOutputStream ();
			oos = new ObjectOutputStream(out);
			
			InputStream in  = socket.getInputStream();
			oin = new ObjectInputStream(in);
			logger.info("Connected.");
		} 
		catch (IOException e) {
			e.printStackTrace();
			logger.severe("Could not create file streams from partA.client thread socket.");
		}
		
		for(int i=0; i<iterationCount; i++) {
			// Pick a random element from list.
			int first = accountIds.get(random.nextInt(accountIds.size()));
			
			// Pick another random element from list until you get a different element.
			int second = accountIds.get(random.nextInt(accountIds.size()));
			while(second == first) {
				second = accountIds.get(random.nextInt(accountIds.size()));
			}
			logger.info(String.format("Initiating Transfer of 10$ from id:%d to id:%d", first, second));
			try {
				oos.writeObject(new TransferRequest(first, second, 10));
				TransferResponse response = (TransferResponse) oin.readObject();
				
				if(response.getStatus().equals(Constants.FAIL_STATUS)) {
					logger.severe(String.format("Transfer request failed. SourceID:%d TargetID:%d", first, second));
					logger.severe(String.format("Failure Reason: %s", response.getMessage()));
				}else {
					logger.info(String.format("Transfer of 10$ from id:%d to id:%d status:%s.", first, second, response.getStatus()));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				logger.severe("Client thread couldn't write TransferRequest to the out stream.");
				logger.severe(e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				logger.severe("Client thread couldn't cast response to TransferRequest.");
				logger.severe(e.getMessage());
			}
		}
		
		try {
			socket.close();
			logger.info("Connection closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
