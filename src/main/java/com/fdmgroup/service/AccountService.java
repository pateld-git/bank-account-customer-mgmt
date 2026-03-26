package com.fdmgroup.service;

import java.util.List;

import com.fdmgroup.dto.account.Account;

public interface AccountService {

	public List<Account> getAllAccounts();
	void removeAccount(Account deleteAccount);
	void createAccount(Account newAccount);

}
