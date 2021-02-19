package shared;

public class TransferResponse extends Response {

	private String status;

	public TransferResponse(int requestId, String operationName, String status) {
		super(requestId, operationName);
		this.setStatus(status);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
