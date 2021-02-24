package tcp.server;

import java.util.Hashtable;
import java.util.logging.Logger;

import tcp.shared.BalanceRequest;
import tcp.shared.BalanceResponse;
import tcp.shared.Constants;
import tcp.shared.CreateAccountRequest;
import tcp.shared.CreateAccountResponse;
import tcp.shared.DepositRequest;
import tcp.shared.DepositResponse;
import tcp.shared.Request;
import tcp.shared.Response;
import tcp.shared.TransferRequest;
import tcp.shared.TransferResponse;

public class RequestHandler {
	
	public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private int threadID;
	
	protected Hashtable<Integer, BankAccount> accounts;
	
	/*
	 * Each thread calls its own request handler object.
	 */
	public RequestHandler(Hashtable<Integer, BankAccount> accounts, int threadID) {
		this.accounts = accounts;
		this.threadID = threadID;
	}
	
	/*
	 * This method processes all kinds of requests.
	 * It calls the right function based on the field operationName
	 */
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
		
		logger.info("t"+this.threadID+": request to transfer amount:" + amount + " from account uid: " + source.UID +" to account uid: " + target.UID);
		TransferResponse response = new TransferResponse(request.requestId, request.getOperationName(), Constants.OK_STATUS);
		
		if(amount <= source.getBalance()) {
			source.setBalance(source.getBalance() - amount);
			target.setBalance(target.getBalance() + amount);
			logger.info("t"+this.threadID+"|r"+request.requestId+": transfer amount:" + amount
					+ " from account uid: " + source.UID +" to account uid: " + target.UID + " status: "+Constants.OK_STATUS);
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
        logger.info("t"+this.threadID+"|r"+request.requestId+": New Account created uid:"+newAccount.UID);
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
