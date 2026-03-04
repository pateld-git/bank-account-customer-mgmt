package model.account;

public abstract class Account {
	private final long ACCOUNT_ID;
	private static long nextAccountID = 1_000;
	protected double balance;
	
	public Account() {
		super();
		ACCOUNT_ID = nextAccountID;
		nextAccountID += 5;
	}
	
	public double withdraw(double amount) {
		balance -= amount;
		return amount;
	}
	
	public void deposit(double amount) {
		balance += amount;
	}
	
	public void correctBalance(double amount) {
		balance = amount;
	}
	
	public long getACCOUNT_ID() {
		return ACCOUNT_ID;
	}
	public double getBalance() {
		return balance;
	}
}
