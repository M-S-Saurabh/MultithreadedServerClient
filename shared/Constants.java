package shared;

public final class Constants {
	private Constants() {
		// restrict instantiation
	}

	public static final String CREATE_ACCOUNT_REQUEST = "create_account";
	public static final String DEPOSIT_REQUEST = "deposit";
	public static final String TRANSFER_REQUEST = "transfer";
	public static final String BALANCE_REQUEST = "balance";
	
	public static final String OK_STATUS = "OK";
	public static final String FAIL_STATUS = "FAIL";
	
	
	public static final String INSUFFICIENT_BALANCE = "In-sufficient balance in source account.";
}
