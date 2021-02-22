package server;

import java.util.List;
import java.util.logging.Logger;

import shared.Constants;
import shared.CreateAccountResponse;
import shared.Request;
import shared.Response;

public class RequestHandler {
	protected List<BankAccount> accounts;
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public RequestHandler(List<BankAccount> accounts) {
		this.accounts = accounts;
	}
	
	Response handle(Request request) {
		String operationName = request.getOperationName();
		logger.info("Request operation name:"+operationName);
		
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
	    		logger.severe("Operation "+operationName+" not implemented. Response is null.");
        }
		return response;
	}
	
	private Response getBalance(Request request) {
		// TODO Auto-generated method stub
		return null;
	}

	private Response createAccount(Request request) {
		BankAccount newAccount = new BankAccount();
        logger.info("New Account created uid:"+newAccount.UID);
        this.accounts.add(newAccount);
        Response response = new CreateAccountResponse(request.requestId, request.getOperationName(), newAccount.UID);
		return response;
	}
}
