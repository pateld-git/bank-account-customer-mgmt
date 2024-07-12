package com.fdmgroup.runner;

import com.fdmgroup.controller.AccountController;
import com.fdmgroup.model.Account;
import com.fdmgroup.model.CheckingAccount;
import com.fdmgroup.model.Company;
import com.fdmgroup.model.Customer;
import com.fdmgroup.model.Person;
import com.fdmgroup.model.SavingsAccount;

public class RunnerSprint1Test {

	public static void main(String[] args) {
		String name = "Karl";
		String address = "4269 Sandblasted Corridors Lane, Hoxes IV DRG 01234";
		String type1 = "company";
		String type11 = "person";
		String type2 = "checking";
		String type21 = "savings";
		AccountController testAC = new AccountController();
		
		// test create Customer and get name, address, and CUSTOMER_ID
		Customer testCust = testAC.createCustomer(name, address, type1);
		System.out.println("\nCreateCustomer: " + testCust.getCustomer_ID() + " " + testCust.getName() + 
				" " + testCust.getAddress());
		
		Company testCust0 = (Company) testAC.createCustomer(name, address, type1);
		System.out.println("CreateCompany: " + testCust0.getCustomer_ID() + " " + testCust0.getName() + 
				" " + testCust0.getAddress());
		
		Person testCust1 = (Person) testAC.createCustomer(name, address, type11);
		System.out.println("CreateCustPerson: " + testCust1.getCustomer_ID() + " " + testCust1.getName() + 
				" " + testCust.getAddress());
		
		// test create account
		Account testAcc = testAC.createAccount(testCust, type2);
		System.out.println("\nCreateAccount: " + testAcc.getAccount_ID() + " " + testAcc.getBalance());
		
		CheckingAccount testAcc0 = (CheckingAccount) testAC.createAccount(testCust, type2);
		System.out.println("CreateCheckingAccount: " + testAcc0.getAccount_ID() + " " + testAcc0.getBalance());
		
		SavingsAccount testAcc1 = (SavingsAccount) testAC.createAccount(testCust1, type21);
		System.out.println("CreateSavingsAccount: " + testAcc1.getAccount_ID() + " " + testAcc1.getBalance());
		
		SavingsAccount testAcc2 = (SavingsAccount) testAC.createAccount(testCust1, type21);
		System.out.println("CreateSavingsAccount: " + testAcc2.getAccount_ID() + " " + testAcc2.getBalance());
		
		// test remove customer and get customers
		//System.out.println(testAC.getCustomers().toString());
		//testAC.removeCustomer(testCust);
		//System.out.println("Remove Customer: " + testAC.getCustomers().toString());
		
		// test remove account and get accounts
		//System.out.println(testAC.getAccounts().toString());
		//testAC.removeAccount(testAcc);
		//System.out.println("Remove Account: " + testAC.getAccounts().toString());
		
		// test set customers
		testCust.setName("John Halo");
		testCust.setAddress("6969 Reach Lane, Milky Way VSC 31337");
		System.out.println("\ntestAC: "+ testCust.getName() + " " + testCust.getAddress());
		
		// test get next check number
		System.out.println("\ngetCheckNumber: "+ testAcc0.getNextCheckNumber());
		System.out.println("getCheckNumber: "+ testAcc0.getNextCheckNumber());
		
		// test withdraw, deposit, correct balance
		System.out.println("\nOriginal Balance: "+ testAcc0.getBalance());
		testAcc0.correctBalance(10_000);
		System.out.println("Correct Balance: "+ testAcc0.getBalance());
		System.out.println("Original Balance: "+ testAcc.getBalance());
		testAcc.deposit(500);
		System.out.println("Deposit Balance: "+ testAcc.getBalance());
		System.out.println("Original Balance: "+ testAcc1.getBalance());
		testAcc1.withdraw(500);
		System.out.println("Withdraw Balance: "+ testAcc1.getBalance());
		testAcc1.withdraw(0);
		System.out.println("Withdraw Balance: "+ testAcc1.getBalance());
		testAcc1.withdraw(-100);
		System.out.println("Withdraw Balance: "+ testAcc1.getBalance());
		
		// test charge all accounts
		testCust.chargeAllAccounts(100);
		System.out.println("\nchargeAllAccounts Balance: "+ testAcc0.getBalance());
		testCust1.chargeAllAccounts(2);
		System.out.println("chargeAllAccounts Balance: "+ testAcc2.getBalance());
		System.out.println("chargeAllAccounts Balance: "+ testAcc1.getBalance());
		
		// add, get, set interest
		System.out.println("Interest: " + testAcc1.getInterestRate());
		testAcc1.addInterest();
		System.out.println("addInterest: "+ testAcc1.getBalance());
		
		testAcc1.setInterestRate(5.01);
		System.out.println("Interest: " + testAcc1.getInterestRate());
		testAcc1.addInterest();
		System.out.println("addInterest: "+ testAcc1.getBalance());

	}

}
