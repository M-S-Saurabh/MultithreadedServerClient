/* Author:  Anand Tripathi - Example program code for CSCI 5105 Spring 2021     */

import java.net.*;
import java.io.*;

public class EchoServer extends Thread {
  protected Socket s;

  EchoServer (Socket s) {
    System.out.println ("New client.");
    this.s = s;
  }

  public void run () {
    try {
      InputStream istream = s.getInputStream ();
      OutputStream ostream = s.getOutputStream ();
      byte buffer[] = new byte[512];
      int count;
      while ((count = istream.read (buffer)) >= 0) {

        String msg = new String( buffer );
        String outMsg = msg.toUpperCase( );
        byte[] outBuf = outMsg.getBytes();

        ostream.write (outBuf, 0, outBuf.length);
        ostream.flush();

        System.out.write (buffer, 0, count);
        System.out.flush();
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

  public static void main (String args[]) throws IOException {

    if (args.length != 1)
         throw new RuntimeException ("Syntax: EchoServer port-number");

    System.out.println ("Starting on port " + args[0]);
    ServerSocket server = new ServerSocket (Integer.parseInt (args[0]));

    while (true) {
      System.out.println ("Waiting for a client request");
      Socket client = server.accept ();
      System.out.println( "Received request from " + client.getInetAddress ());
      System.out.println( "Starting worker thread..." );
      EchoServer c = new EchoServer (client);
      c.start ();
    }
  }
}
