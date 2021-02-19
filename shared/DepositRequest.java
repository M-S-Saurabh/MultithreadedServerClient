package shared;

public class DepositRequest extends Request {

	private int uid;
	private int amount;

	public DepositRequest(int uid, int amount) {
		super(Constants.DEPOSIT_REQUEST);
		this.setUid(uid);
		this.setAmount(amount);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
}
