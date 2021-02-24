package tcp.server;
/*
 * The main data-structure to store details of a bank account.
 * Exposes basic account operations which are listed in requirements.
 */
public class BankAccount {
	private static int uidCounter = 0;
	public final int UID;
	private int balance;
	
	public BankAccount() {
		this.UID = ++uidCounter;
		this.setBalance(0);
	}
	
	public void deposit(int amount) {
		this.setBalance(this.getBalance() + amount);
	}
	
	public void withdraw(int amount) {
		this.setBalance(this.getBalance() - amount);
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public int getAccID() {
		return this.UID;
	}
}
