package dao.customer;

import java.util.List;

import dao.account.Account;

public class Person extends Customer {
	public Person(String name, String address) {
		super(name, address);
	}
	
	@Override
	public void chargeAllAccounts(double amount) {
		List<Account> allAccounts = getAccounts();
		for (Account a : allAccounts) { 
			a.withdraw(amount); 
		}	
	}
}
