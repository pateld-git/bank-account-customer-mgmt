package model;

import java.util.List;

public abstract class Customer {
	private final long CUSTOMER_ID;
	private static long nextCustomerId;
	private String name;
	private String address;
	private List<Account> accounts;
	
	public Customer(String name, String address) {
		this.CUSTOMER_ID = 0;
		this.name = name;
		this.address = address;
	}
	
	public void addAccount(Account account) {
		
	}
	
	public void removeAccount(Account account) {
		
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
	public long getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public List<Account> getAccounts() {
		return accounts;
	}
}
