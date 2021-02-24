/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partA.shared;

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
