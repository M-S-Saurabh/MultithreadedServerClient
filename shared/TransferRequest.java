package shared;

public class TransferRequest extends Request {

	private int source;
	private int target;
	private int amount;

	public TransferRequest(int source, int target, int amount) {
		super(Constants.TRANSFER_REQUEST);
		this.setSource(source);
		this.setTarget(target);
		this.setAmount(amount);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}
	
}
