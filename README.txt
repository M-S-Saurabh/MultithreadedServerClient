HOW TO RUN:
------------------------------------------------------------------------------------------------------
TCP Server Client
-----------------

1. CD into the main directory with the name "MultithreadedServerClient"

2. To compile server and client programs use the following commands:
		javac ./tcp/server/TCPServer.java
		javac ./tcp/client/TCPClient.java

3. First, run the TCP server using:
		java tcp.server.TCPServer <port-number>

   Here, replace <port-number> with any integer port number. For example:
   		java tcp.server.TCPServer 9091

4. Then run the TCP client use:
		java tcp.client.TCPClient <host-name> <port-number> <thread-count> <iteration-count>

   Replace respective options in this command. For example:
   		java tcp.client.TCPClient localhost 9091 10 5

RMI Server Client
-----------------

1. CD into the main directory with the name "MultithreadedServerClient"

2. To compile server and client programs use the following commands:
		javac ./rmi/RMIBankServerImpl.java
		javac ./rmi/RMIClient.java

3. First, run the rmiregistry on some port using:
		rmiregistry <port-number>

	Example: rmiregistry 1099
	Remember this should be run in the main directory which has the name "MultithreadedServerClient".

4. Next run the RMI server using the same port number which is used to run rmiregistry:
		java rmi.RMIBankServerImpl <port-number>

	Example: java rmi.RMIBankServerImpl 1099

5. Finally run the RMI client using the command:
		java rmi.RMIClient <host-name> <port-number> <thread-count> <iteration-count>

	Example: java rmi.RMIClient localhost 1099 10 5

------------------------------------------------------------------------------------------------------

NOTES:
------
- All log files are stored in the directory named 'MultithreadedServerClient/logs'
- Logs are also printed on command line during execution.
- Tested code on CSE lab machine:
		csel-kh1260-20.cselabs.umn.edu


