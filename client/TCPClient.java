package client;
/* Author:  Anand Tripathi - Example program code for CSCI 5105 Spring 2021     */

import java.net.*;
import java.io.*;

public class TCPClient   {

  protected String host, file;
  protected int port;

  public static void main (String args[]) throws IOException {
    InetAddress  server  = null;
    Socket      sock = null;

    if ( args.length != 2 ) {
       throw new RuntimeException( "hostname and port number as arguments" );
    }

    String host = args[0];
    int  port = Integer.parseInt( args[1] );

    System.out.println ("Connecting to " + host + ":" + port + "..");

    Socket socket = new Socket (host, port);
    System.out.println ("Connected.");

    OutputStream rawOut = socket.getOutputStream ();
    InputStream rawIn = socket.getInputStream ();
    BufferedReader  buffreader = new BufferedReader( new InputStreamReader(rawIn) );
    PrintWriter serverWriter = new PrintWriter(new OutputStreamWriter(rawOut));

    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

    String line ;
    while ( ( line = keyboard.readLine() ) != null ) {
            serverWriter.println( line );
            serverWriter.flush();
    }

    socket.shutdownOutput();
   
/*
    while ( ( line = buffreader.readLine() ) != null ) {
            System.out.println( line );
    }
*/

    while ( buffreader.ready())   {
          if ( (line = buffreader.readLine() ) != null ) {
            System.out.println( line );
          }
  }
}
}
