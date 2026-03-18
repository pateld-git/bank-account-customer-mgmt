package com.fdmgroup.dto.account;

public class SavingsAccount extends Account {
	private double interestRate;

	public SavingsAccount() {
		super();
		interestRate = 0.39;
		balance = 500;
	}

	public void addInterest() {
		balance += (getBalance() * interestRate) / 100;
	}

	@Override
	public double withdraw(double amount) {
		if (amount > getBalance()) {
			return 0;
		}

		return super.withdraw(amount);
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	@Override
	public String toString() {
		return "SavingsAccount [ACCOUNT_ID=" + getACCOUNT_ID() + ", balance=" + balance + ", interestRate="
				+ interestRate + "]";
	}

}
