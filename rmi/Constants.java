package rmi;


public final class Constants {
	private Constants() {
		// restrict instantiation
	}
	
	// Logging related
	public static final String LOG_FORMAT = "%1$tF %1$tT %4$s %2$s %5$s%6$s%n";
	
	// Status message strings
	public static final String OK_STATUS = "OK";
	public static final String FAIL_STATUS = "FAIL";
	
	// Failure reason strings
	public static final String INSUFFICIENT_BALANCE = "In-sufficient balance in source account.";
	
	// RMI stub name used on registry.
	public static final String RMI_SERVER_NAME = "MyRMIBankServer";
	
}