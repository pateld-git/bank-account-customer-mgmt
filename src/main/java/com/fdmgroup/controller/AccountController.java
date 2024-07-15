package com.fdmgroup.controller;

import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.model.Account;
import com.fdmgroup.model.CheckingAccount;
import com.fdmgroup.model.Company;
import com.fdmgroup.model.Customer;
import com.fdmgroup.model.Person;
import com.fdmgroup.model.SavingsAccount;

public class AccountController {
	/*---------------------------------------------------------------------------------------------------------------*/
	/* Class Attributes */
	/*---------------------------------------------------------------------------------------------------------------*/
	private List<Customer> customers = new ArrayList<>();
	private List<Account> accounts = new ArrayList<>();

	/*---------------------------------------------------------------------------------------------------------------*/
	/* Class Methods */
	/*---------------------------------------------------------------------------------------------------------------*/

	// create a new customer and add it to the list of customers
	public Customer createCustomer(String name, String address, String type) {

		// Create a person customer type
		if (type.contentEquals("person")) {
			Person newCustomer = new Person(name, address);

			customers.add((Customer) newCustomer);
			return (Customer) newCustomer;
		}

		// Create a company customer type
		else if (type.contentEquals("company")) {
			Company newCustomer = new Company(name, address);

			customers.add((Customer) newCustomer);
			return (Customer) newCustomer;
		}

		// Invalid type
		else {
			return null;
		}
	}

	// create an account and add it to the list of accounts and the specified
	// cusomter's list of accounts
	public Account createAccount(Customer customer, String type) {

		// Create a checking account type
		if (type.contentEquals("checking")) {
			CheckingAccount newAccount = new CheckingAccount();

			accounts.add((Account) newAccount);
			customer.addAccount(newAccount);

			return (Account) newAccount;
		}

		// Create a savings account type
		else if (type.contentEquals("savings")) {
			SavingsAccount newAccount = new SavingsAccount();

			accounts.add((Account) newAccount);
			customer.addAccount(newAccount);

			return (Account) newAccount;
		}

		// Invalid type
		else {
			return null;
		}
	}

	// remove the customer from the list of accounts
	public void removeCustomer(Customer customer) {
		customers.remove(customer);
	}

	// remove the account from the list of accounts and remove the account from its
	// associated customer
	public void removeAccount(Account account/* , Customer customer */) {
		accounts.remove(account);

		// cycle through the list of customers to find an account that matches the
		// account to be removed
		for (Customer c : customers) {

			// check if the customer account list contains a matching account
			if (c.getAccounts().contains(account)) {

				// remove the matching account from the specified customer's account list
				c.removeAccount(account);
			}
		}
		// customer.removeAccount(account);
	}

	/*---------------------------------------------------------------------------------------------------------------*/
	/* Getter and Setter Methods */
	/*---------------------------------------------------------------------------------------------------------------*/
	public List<Customer> getCustomers() {
		return customers;
	}

	public List<Account> getAccounts() {
		return accounts;
	}
}
