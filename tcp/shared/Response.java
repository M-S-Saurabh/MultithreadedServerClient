package tcp.shared;

import java.io.Serializable;

public class Response implements Serializable {
	public final int requestId;
	private String operationName;
	private String message;
	
	public Response(int requestId, String operationName) {
		this.requestId = requestId;
		setOperationName(operationName);
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
