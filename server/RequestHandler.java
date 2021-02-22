package server;

import java.util.List;

import shared.Constants;
import shared.CreateAccountResponse;
import shared.Request;
import shared.Response;

public class RequestHandler {
	protected List<BankAccount> accounts;
	
	public RequestHandler(List<BankAccount> accounts) {
		this.accounts = accounts;
	}
	
	Response handle(Request request) {
		String operationName = request.getOperationName();
		System.out.println("Request operation name:"+operationName);
		
		Response response = null;
        switch(operationName) 
        {
	        case Constants.CREATE_ACCOUNT_REQUEST:
	        	response = createAccount(request);
	        	break;
	        case Constants.BALANCE_REQUEST:
	        	response = getBalance(request);
	        	break;
	    	/*
	    	 * .
	    	 * .
	    	 * .
	    	 * more methods here.
	    	 */
	    	default:
	    		System.out.println("Method not implemented. Response");
        }
		return response;
	}
	
	private Response getBalance(Request request) {
		// TODO Auto-generated method stub
		return null;
	}

	private Response createAccount(Request request) {
		BankAccount newAccount = new BankAccount();
        System.out.println("New Account created uid:"+newAccount.UID);
        this.accounts.add(newAccount);
        Response response = new CreateAccountResponse(request.requestId, request.getOperationName(), newAccount.UID);
		return response;
	}
}
