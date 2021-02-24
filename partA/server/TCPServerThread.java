/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partA.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.logging.Logger;

import partA.shared.Request;
import partA.shared.Response;

public class TCPServerThread implements Runnable {

	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private static int THREADCOUNT = 1;
	
	// Just an id to uniquely identify thread. Used to identify which thread wrote the log.
	private int threadID;
	
	protected Socket s;
	protected RequestHandler requestHandler;
	

	TCPServerThread (Socket s, Hashtable<Integer, BankAccount> accounts) {
		logger.info("ServerThread created for new partA.client.");
		this.s = s;
		this.threadID = THREADCOUNT;
		this.requestHandler = new RequestHandler(accounts, this.threadID);
		THREADCOUNT++;
	}

	public void run () {
		try {
			InputStream istream = s.getInputStream ();
			ObjectInputStream ois = new ObjectInputStream( istream );
			
			OutputStream ostream = s.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream( ostream );
			
			try
			{	// Infinite loop until all client requests are processed.
			    for (;;)
			    {
			        Object object = ois.readObject();
			        Request request = (Request) object;
			        Response response = this.requestHandler.handle(request);
			        oos.writeObject(response);
			    }
			}
			catch (SocketTimeoutException exc)
			{
			    // you got the timeout
				logger.severe("t"+this.threadID+"|: connection timeout");
				exc.printStackTrace();
				logger.severe(exc.getMessage());
			}
			catch (EOFException exc)
			{
			    // end of stream
				logger.info("t"+this.threadID+"|: End of Object stream is reached. Done with all requests from partA.client.");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				logger.severe(e.getMessage());
			}
			
			logger.info("t|"+this.threadID+"|: Client exit. ServerThread exit.");
			s.close();
		} catch (IOException ex) {
			ex.printStackTrace ();
			logger.severe("t"+this.threadID+"|: "+ex.getMessage());
		} finally {
			try {
				s.close ();
			} catch (IOException ex) {
				ex.printStackTrace ();
				logger.severe("t"+this.threadID+"|: "+ex.getMessage());
			}
		}
	}
}
