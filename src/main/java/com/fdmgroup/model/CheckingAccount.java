package com.fdmgroup.model;

public class CheckingAccount extends Account{
	private int nextCheckNumber = 1;
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Class Constructors*/
	/*---------------------------------------------------------------------------------------------------------------*/
	public CheckingAccount() {
		super();
	}
	
	public CheckingAccount(double balance) {
		super(balance);
	}
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Getter and Setter Methods*/
	/*---------------------------------------------------------------------------------------------------------------*/
	public int getNextCheckNumber() {
		
		// return the value of nextCheckNumber then increment it and save it to nextCheckNumber
		return nextCheckNumber++;
	}
}
