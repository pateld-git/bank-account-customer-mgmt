package controller;

import java.util.List;

import model.Account;
import model.Company;
import model.Customer;
import model.Person;

public class AccountController {
	private List<Customer> customers;
	private List<Account> accounts;
	
	public Customer createCustomer(String name, String address, String type) {
		if(type.toLowerCase() == "person") {
			Person person = new Person(name, address);
			customers.add(person);
			return person;
		}
		else if(type.toLowerCase().trim() == "company"){
			Company company  = new Company(name, address);
			customers.add(company);
			return company;
		}
		else
			return null;
	}
	
	public Account createAccount(Customer customer, String type) {
		return null;
		
	}
	
	public void removeCustomer(Customer customer) {
		customers.remove(customer);
		
	}
	
	public void removeAccount(Account account) {
		accounts.remove(account);
		
	}

	public List<Customer> getCustomers() {
		return customers;
	}
	public List<Account> getAccounts() {
		return accounts;
	}	
}
	

