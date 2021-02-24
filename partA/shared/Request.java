/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partA.shared;

import java.io.Serializable;

public class Request implements Serializable{
	transient private static int requestCounter = 0;
	public final int requestId;
	private String operationName;
	
	public Request(String operationName) {
		setOperationName(operationName);
		this.requestId = requestCounter;
		requestCounter++;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
}
