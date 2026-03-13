package model.account;

public abstract class Account {
	private final long accountID;
	private static long nextAccountID = 1_000;
	protected double balance;
	
	protected Account() {
		super();
		accountID = nextAccountID;
		nextAccountID += 5;
		balance = 0;
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
	
	public long getaccountID() {
		return accountID;
	}
	public double getBalance() {
		return balance;
	}
}
