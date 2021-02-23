package tcp.shared;

public class CreateAccountResponse extends Response {

	private int uid;

	public CreateAccountResponse(int requestId, String operationName, int uid) {
		super(requestId, operationName);
		this.setUid(uid);
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
}