package model.account;

public class SavingsAccount extends Account {
	private double interestRate;
	
	public SavingsAccount() {
		super();
		interestRate = 0.39;
<<<<<<< HEAD
		balance = 500;
=======
>>>>>>> branch 'main' of https://git.fdmgroup.com/Duncan.Patel/bank-account-customer-mgmt.git
	}
	
	public void addInterest() {
		balance += (getBalance() * interestRate) / 100;
	}

	@Override
	public double withdraw(double amount) {
		if(amount > getBalance()) {
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
}
