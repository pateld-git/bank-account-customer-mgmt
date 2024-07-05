package com.fdmgroup.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Customer {
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Class Attributes*/
	/*---------------------------------------------------------------------------------------------------------------*/
	// unique customer ID number
	private final long CUSTOMER_ID;
	
	// generates unique customer ID upon account creation
	private static AtomicInteger nextCustomer_ID = new AtomicInteger(2_000_000);
	
	// customer name
	private String name;
	
	// customer mailing address
	private String address;
	
	// list of all the customer's accounts
	private List<Account> accounts = new ArrayList<>();
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Class Constructors*/
	/*---------------------------------------------------------------------------------------------------------------*/
	public Customer(String name, String address) {
		int idIncrement = 7;
		
		this.CUSTOMER_ID = nextCustomer_ID.getAndAdd(idIncrement);
		this.name = name;
		this.address = address;
	}
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Class Methods*/
	/*---------------------------------------------------------------------------------------------------------------*/
	public void addAccount(Account account) {
		accounts.add(account);
	}
	
	public void removeAccount(Account account) {
		accounts.remove(account);
	}
	
	public abstract void chargeAllAccounts(double amount);
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Getter and Setter Methods*/
	/*---------------------------------------------------------------------------------------------------------------*/
	public List<Account> getAccounts(){
		return accounts;
	}
	
	public long getCustomer_ID() {
		return CUSTOMER_ID;
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
}
