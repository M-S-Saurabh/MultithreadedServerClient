package shared;

public class BalanceResponse extends Response {

	private int balance;
	private int uid;

	public BalanceResponse(int requestId, int uid, String operationName, int balance) {
		super(requestId, operationName);
		this.setUid(uid);
		this.setBalance(balance);
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public int getUid() {
		return this.uid;
	}
	
	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
	
}
