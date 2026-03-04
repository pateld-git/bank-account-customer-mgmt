package model.account;

public class CheckingAccount extends Account {
	private int nextCheckNumber;
	
	public CheckingAccount() {
		super();
		nextCheckNumber = 1;
	}
	
	public int getNextCheckNumber() {
		return nextCheckNumber++;
	}
}
