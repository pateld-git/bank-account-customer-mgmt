package com.fdmgroup.dto.account;

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

	@Override
	public String toString() {
		return "CheckingAccount [ACCOUNT_ID=" + getACCOUNT_ID() + ", balance=" + balance + ", nextCheckNumber="
				+ nextCheckNumber + "]";
	}

}
