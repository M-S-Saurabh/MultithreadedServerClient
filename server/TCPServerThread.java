package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

import shared.CreateAccountResponse;
import shared.Request;
import shared.Response;

public class TCPServerThread implements Runnable {

	protected Socket s;
	protected List<BankAccount> accounts;

	TCPServerThread (Socket s, List<BankAccount> accounts) {
		System.out.println ("New client.");
		this.s = s;
		this.accounts = accounts;
	}

	public void run () {
		try {
			InputStream istream = s.getInputStream ();
			ObjectInputStream ois = new ObjectInputStream( istream );
			
			OutputStream ostream = s.getOutputStream ();
			ObjectOutputStream oos = new ObjectOutputStream( ostream );
			
			try
			{
			    for (;;)
			    {
			        Object object = ois.readObject();
			        Request req = (Request) object;
			        System.out.println("Request operation name:"+req.getOperationName());
			        BankAccount newAccount = new BankAccount();
			        System.out.println("New Account created uid:"+newAccount.UID);
			        this.accounts.add(newAccount);
			        
			        Response response = new CreateAccountResponse(req.requestId, req.getOperationName(), newAccount.UID);
			        oos.writeObject(response);
			    }
			}
			catch (SocketTimeoutException exc)
			{
			    // you got the timeout
				exc.printStackTrace();
			}
			catch (EOFException exc)
			{
			    // end of stream
				System.out.println("End of Object stream is reached.");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			System.out.println ("Client exit.");
			s.close();
		} catch (IOException ex) {
			ex.printStackTrace ();
		} finally {
			try {
				s.close ();
			} catch (IOException ex) {
				ex.printStackTrace ();
			}
		}
	}
}
