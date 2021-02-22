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
	protected RequestHandler requestHandler;

	TCPServerThread (Socket s, List<BankAccount> accounts) {
		System.out.println("New client.");
		this.s = s;
		this.requestHandler = new RequestHandler(accounts);
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
			        Request request = (Request) object;
			        Response response = this.requestHandler.handle(request);
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
