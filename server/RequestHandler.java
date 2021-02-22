package server;

import java.util.Hashtable;
import java.util.logging.Logger;

import shared.Constants;
import shared.BalanceRequest;
import shared.BalanceResponse;
import shared.CreateAccountRequest;
import shared.CreateAccountResponse;
import shared.DepositRequest;
import shared.DepositResponse;
import shared.Request;
import shared.Response;

public class RequestHandler {
	
	protected Hashtable<Integer, BankAccount> accounts;
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public RequestHandler(Hashtable<Integer, BankAccount> accounts) {
		this.accounts = accounts;
	}
	
	Response handle(Request request) {
		String operationName = request.getOperationName();
		logger.info("Request operation name:"+operationName);
		
		Response response = null;
        switch(operationName) 
        {
	        case Constants.CREATE_ACCOUNT_REQUEST:
	        	response = createAccount((CreateAccountRequest) request);
	        	break;
	        case Constants.BALANCE_REQUEST:
	        	response = getBalance((BalanceRequest) request);
	        	break;
	        case Constants.DEPOSIT_REQUEST:
	        	response = deposit((DepositRequest) request);
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
	
	private Response getBalance(BalanceRequest request) {
		// TODO Auto-generated method stub
		int accID = request.getUid();
		int balance = accounts.get(accID).getBalance();
		Response response = new BalanceResponse(request.requestId, accID, request.getOperationName(), balance);
		return response;
	}

	private Response createAccount(CreateAccountRequest request) {
		BankAccount newAccount = new BankAccount();
        logger.info("New Account created uid:"+newAccount.UID);
        this.accounts.put(newAccount.UID, newAccount);
        Response response = new CreateAccountResponse(request.requestId, request.getOperationName(), newAccount.UID);
		return response;
	}
	
	private Response deposit(DepositRequest request) {
		int accID = request.getUid();
		int amount = request.getAmount();
		accounts.get(accID).setBalance(amount);
		Response response = new DepositResponse(request.requestId, accID, request.getOperationName(), Constants.OK_STATUS);
		return response;
	}
}
