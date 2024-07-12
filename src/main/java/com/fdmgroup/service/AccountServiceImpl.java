package com.fdmgroup.service;

import java.util.List;

import com.fdmgroup.DAO.AccountReaderDAO;
import com.fdmgroup.DAO.AccountWriterDAO;
import com.fdmgroup.model.Account;

public class AccountServiceImpl implements AccountService{

	private AccountReaderDAO accountReaderDAO;
	private AccountWriterDAO accountWriterDAO;
	
	
	
	public AccountServiceImpl(AccountReaderDAO accountReaderDAO, AccountWriterDAO accountWriterDAO) {
		super();
		this.accountReaderDAO = accountReaderDAO;
		this.accountWriterDAO = accountWriterDAO;
	}
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Implemented Methods*/
	/*---------------------------------------------------------------------------------------------------------------*/
	@Override
	public List<Account> getAccounts() {
		return accountReaderDAO.readAccounts();
	}

	@Override
	public void removeAccount(Account account) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Account createAccount(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

}
