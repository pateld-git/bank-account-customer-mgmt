package com.fdmgroup.model;

public class SavingsAccount extends Account{
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Class Attributes*/
	/*---------------------------------------------------------------------------------------------------------------*/
	private double interestRate;
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Class Constructors*/
	/*---------------------------------------------------------------------------------------------------------------*/
	public SavingsAccount() {
		super();
		this.interestRate = 1.05;
	}
	
	public SavingsAccount(double balance) {
		super(balance);
		this.interestRate = 1.05;
	}
	
	public SavingsAccount(double balance, double interestRate) {
		super(balance);
		this.interestRate = interestRate;
	}
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Class Methods*/
	/*---------------------------------------------------------------------------------------------------------------*/
	
	// Withdraws money from the account balance
	public double withdraw(double amount) {
		
		// give money if balance is greater than or equal to withdrawal amount
		if(this.balance >= amount) {
			this.balance -= amount;
			return this.balance;
		}
		
		// give nothing otherwise
		else {
			return 0;
		}
	}
	
	// Calculates and adds interest to the balance amount
	public void addInterest() {
		double interestDue = (this.balance * interestRate) / 100;
		this.balance += interestDue;
	}
	
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Getter and Setter Methods*/
	/*---------------------------------------------------------------------------------------------------------------*/
	public double getInterestRate() {
		return this.interestRate;
	}
	
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
}
