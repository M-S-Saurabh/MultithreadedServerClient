package shared;

public class BalanceResponse extends Response {

	private int balance;

	public BalanceResponse(int requestId, String operationName, int balance) {
		super(requestId, operationName);
		this.setBalance(balance);
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
}
