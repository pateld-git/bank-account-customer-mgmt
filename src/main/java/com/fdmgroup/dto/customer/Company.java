package com.fdmgroup.dto.customer;

import java.util.List;

import com.fdmgroup.dto.account.Account;
import com.fdmgroup.dto.account.CheckingAccount;
import com.fdmgroup.dto.account.SavingsAccount;



public class Company extends Customer {
	public Company(String name, String address) {
		super(name, address);
	}

	@Override
	public void chargeAllAccounts(double amount) {
		List<Account> allAccounts = getAccounts();
		
		for (Account a : allAccounts) {
			if(a instanceof CheckingAccount) { 
				a.withdraw(amount); 
			}
			else if(a instanceof SavingsAccount) { 
				a.withdraw(2 * amount); 
			}
		}
		
	}
}
