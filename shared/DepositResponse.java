package shared;

public class DepositResponse extends Response {

	private String status;
	private int uid;

	public DepositResponse(int requestId, int uid, String operationName, String status) {
		super(requestId, operationName);
		this.setStatus(status);
		this.setUid(uid);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return this.uid;
	}
}
