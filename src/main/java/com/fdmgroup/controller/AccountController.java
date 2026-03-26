package com.fdmgroup.controller;

import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.dto.account.Account;
import com.fdmgroup.dto.account.CheckingAccount;
import com.fdmgroup.dto.account.SavingsAccount;
import com.fdmgroup.dto.customer.Company;
import com.fdmgroup.dto.customer.Customer;
import com.fdmgroup.dto.customer.Person;

public class AccountController {
	private List<Customer> customers;
    private List<Account> accounts;
    
	public AccountController() {
		super();
		customers = new ArrayList<>();
	    accounts = new ArrayList<>();
	}

	public Customer createCustomer(String name, String address, String type) {
		if(type.trim().equalsIgnoreCase("person")) {
			Person person = new Person(name, address);
			customers.add(person);
			return person;
		}
		else if(type.trim().equalsIgnoreCase("company")) {
			Company company  = new Company(name, address);
			customers.add(company);
			return company;
		}

		return null;
	}
	
	public Account createAccount(Customer customer, String type) {
		if(type.trim().equalsIgnoreCase("checking")) {
			CheckingAccount checking = new CheckingAccount();
			accounts.add(checking);
			customer.addAccount(checking);
			return checking;
		}
		else if(type.trim().equalsIgnoreCase("savings")) {
			SavingsAccount savings = new SavingsAccount();
			accounts.add(savings);
			customer.addAccount(savings);
			return savings;
		}
		
		return null;
	}
	
	public void removeCustomer(Customer customer) {
		customers.remove(customer);
		for (Account account : customer.getAccounts()) {
				accounts.remove(account);
		}
	}
	
	public void removeAccount(Account account) {
		accounts.remove(account);
		for(Customer customer : customers) {
			customer.removeAccount(account);
		}
	}

	public List<Customer> getCustomers() {
		return customers;
	}
	public List<Account> getAccounts() {
		return accounts;
	}	
}
	

