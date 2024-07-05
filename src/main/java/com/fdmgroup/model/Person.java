package com.fdmgroup.model;

import java.util.List;

public class Person extends Customer{
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Class Constructors*/
	/*---------------------------------------------------------------------------------------------------------------*/
	public Person(String name, String address) {
		super(name, address);
	}
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Class Methods*/
	/*---------------------------------------------------------------------------------------------------------------*/
	// charge all accounts of specified person
	public void chargeAllAccounts(double charge) {
		List<Account> personAccounts =  getAccounts();
		
		for(Account a : personAccounts) {
			a.withdraw(charge);
		}
	}
}
