package model;

public abstract class Account {
	private final long ACCOUNT_ID;
	private static long nextAccountID;
	double balance;
	
	public Account() {
		this.ACCOUNT_ID = 0;
	}
	
	public double withdraw(double amount) {
		return 0;
	}
	
	public void deposit(double amount) {
	}
	
	public void correctBalance(double amount) {
	}
	
	public long getACCOUNT_ID() {
		return ACCOUNT_ID;
	}
	public double getBalance() {
		return balance;
	}
}
