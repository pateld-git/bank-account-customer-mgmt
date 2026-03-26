package com.fdmgroup.model.account;

public class CheckingAccount extends Account {
	private int nextCheckNumber;
	
	public CheckingAccount() {
		super();
		nextCheckNumber = 1;
		balance = 250;
	}
	
	public int getNextCheckNumber() {
		return nextCheckNumber++;
	}
}
