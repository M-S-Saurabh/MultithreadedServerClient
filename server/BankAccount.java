package server;

public class BankAccount {
	static int uidCounter = 0;
	public final int UID;
	private int balance;
	
	public BankAccount() {
		this.UID = uidCounter;
		uidCounter++;
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
	
}
