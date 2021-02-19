package shared;

import java.io.Serializable;

public class Response implements Serializable {
	public final int requestId;
	private String operationName;
	
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
}
