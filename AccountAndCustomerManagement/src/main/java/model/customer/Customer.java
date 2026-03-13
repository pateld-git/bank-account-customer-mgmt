package model.customer;

import java.util.ArrayList;
import java.util.List;

import model.account.Account;

public abstract class Customer {
	private final long customerID;
	private static long nextCustomerId = 2_000_000;
	private String name;
	private String address;
	private List<Account> accounts;
	
	public abstract void chargeAllAccounts(double amount);
	
	protected Customer(String name, String address) {
		super();
		customerID = nextCustomerId;
		nextCustomerId += 7;
		this.name = name;
		this.address = address;
		accounts = new ArrayList<>();
	}
	
	public void addAccount(Account account) {
		accounts.add(account);
	}
	
	public void removeAccount(Account account) {
		accounts.remove(account);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long customerID() {
		return customerID;
	}
	public List<Account> getAccounts() {
		return accounts;
	}
}
