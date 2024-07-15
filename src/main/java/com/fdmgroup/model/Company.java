package com.fdmgroup.model;

import java.util.List;

public class Company extends Customer {
	/*---------------------------------------------------------------------------------------------------------------*/
	/* Class Constructors */
	/*---------------------------------------------------------------------------------------------------------------*/
	public Company(String name, String address) {
		super(name, address);
	}

	/*---------------------------------------------------------------------------------------------------------------*/
	/* Class Methods */
	/*---------------------------------------------------------------------------------------------------------------*/
	// charge all accounts of specified company
	public void chargeAllAccounts(double charge) {
		List<Account> companyAccounts = getAccounts();

		for (Account a : companyAccounts) {
			a.withdraw(charge);
		}
	}
}
