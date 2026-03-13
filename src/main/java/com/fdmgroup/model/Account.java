package com.fdmgroup.model;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Account {
	/*---------------------------------------------------------------------------------------------------------------*/
	/* Class Attributes */
	/*---------------------------------------------------------------------------------------------------------------*/
	// unique account ID number
	private final long ACCOUNT_ID;

	// generates unique account ID upon account creation
	private static AtomicInteger nextAccountID = new AtomicInteger(1_000);

	// current account balance
	protected double balance;

	/*---------------------------------------------------------------------------------------------------------------*/
	/* Class Constructors */
	/*---------------------------------------------------------------------------------------------------------------*/
	protected Account() {
		int idIncrement = 5;

		this.ACCOUNT_ID = nextAccountID.getAndAdd(idIncrement);
		this.balance = 0.00;
	}

	protected Account(double balance) {
		int idIncrement = 5;

		this.ACCOUNT_ID = nextAccountID.getAndAdd(idIncrement);
		this.balance = balance;
	}

	/*---------------------------------------------------------------------------------------------------------------*/
	/* Class Methods */
	/*---------------------------------------------------------------------------------------------------------------*/
	public double withdraw(double amount) {
		this.balance -= amount;
		return this.balance;
	}

	public void deposit(double amount) {
		this.balance += amount;
	}

	public void correctBalance(double amount) {
		this.balance = amount;
	}

	/*---------------------------------------------------------------------------------------------------------------*/
	/* Getter and Setter Methods */
	/*---------------------------------------------------------------------------------------------------------------*/
	public long getAccount_ID() {
		return ACCOUNT_ID;
	}

	public double getBalance() {
		return balance;
	}
}
