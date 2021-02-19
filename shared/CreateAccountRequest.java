package shared;

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
