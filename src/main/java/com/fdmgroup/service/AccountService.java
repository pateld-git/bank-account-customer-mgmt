package com.fdmgroup.service;

import java.util.List;

import com.fdmgroup.model.Account;

public interface AccountService {
	public List<Account> getAccounts();

	public void removeAccount(Account account);

	public Account createAccount(Account account);

}
