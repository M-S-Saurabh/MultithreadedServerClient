/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partA.shared;

public class CreateAccountRequest extends Request {

	public CreateAccountRequest() {
		super(Constants.CREATE_ACCOUNT_REQUEST);
	}
	
	@Override
    public String toString() {
        return "CreateAccountRequest [requestId=" + requestId 
        		+ ", operationName=" + this.getOperationName() + "]";
    }
	
}
