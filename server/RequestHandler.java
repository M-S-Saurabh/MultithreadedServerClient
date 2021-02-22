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
import shared.TransferRequest;
import shared.TransferResponse;

public class RequestHandler {
	
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private int threadID;
	
	protected Hashtable<Integer, BankAccount> accounts;
	
	
	public RequestHandler(Hashtable<Integer, BankAccount> accounts, int threadID) {
		this.accounts = accounts;
		this.threadID = threadID;
	}
	
	Response handle(Request request) {
		String operationName = request.getOperationName();
		logger.finer("t"+this.threadID+"|r"+request.requestId+": Request operation name:"+operationName);
		
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
	        case Constants.TRANSFER_REQUEST:
	        	response = transfer((TransferRequest) request);
	        	break;
	    	default:
	    		logger.severe("t"+this.threadID+"|r"+request.requestId+": Operation "+operationName+" not implemented. Response is null.");
        }
		return response;
	}
	
	private Response transfer(TransferRequest request) {
		BankAccount source = accounts.get(request.getSource());
		BankAccount target = accounts.get(request.getTarget());
		int amount = request.getAmount();
		
		TransferResponse response = new TransferResponse(request.requestId, request.getOperationName(), Constants.OK_STATUS);
		
		if(amount <= source.getBalance()) {
			source.setBalance(source.getBalance() - amount);
			target.setBalance(target.getBalance() + amount);
		}else {
			response.setStatus(Constants.FAIL_STATUS);
			response.setMessage(Constants.INSUFFICIENT_BALANCE);
		}
		return response;
	}

	private Response getBalance(BalanceRequest request) {
		int accID = request.getUid();
		int balance = accounts.get(accID).getBalance();
		Response response = new BalanceResponse(request.requestId, accID, request.getOperationName(), balance);
		return response;
	}

	private Response createAccount(CreateAccountRequest request) {
		BankAccount newAccount = new BankAccount();
        logger.fine("t"+this.threadID+"|r"+request.requestId+": New Account created uid:"+newAccount.UID);
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
