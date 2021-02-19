package shared;

public class DepositResponse extends Response {

	private String status;

	public DepositResponse(int requestId, String operationName, String status) {
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
