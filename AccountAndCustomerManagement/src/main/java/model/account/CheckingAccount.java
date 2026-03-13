package model.account;

public class CheckingAccount extends Account {
	private int nextCheckNumber;
	
	public CheckingAccount() {
		super();
		nextCheckNumber = 1;
<<<<<<< HEAD
		balance = 250;
=======
>>>>>>> branch 'main' of https://git.fdmgroup.com/Duncan.Patel/bank-account-customer-mgmt.git
	}
	
	public int getNextCheckNumber() {
		return nextCheckNumber++;
	}
}
