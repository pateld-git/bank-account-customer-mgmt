package model;

import java.util.List;

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
