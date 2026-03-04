package model;

public class SavingsAccount extends Account {
	private double interestRate;
	
	public SavingsAccount() {
		super();
	}
	
	public void addInterest() {
		
	}

	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
}
