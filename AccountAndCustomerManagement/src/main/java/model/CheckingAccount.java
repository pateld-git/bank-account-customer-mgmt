package model;

public class CheckingAccount extends Account {
	public int nextCheckNumber;
	public CheckingAccount() {
		super();
	}
	
	public int getNextCheckNumber() {
		return nextCheckNumber;
	}
}
