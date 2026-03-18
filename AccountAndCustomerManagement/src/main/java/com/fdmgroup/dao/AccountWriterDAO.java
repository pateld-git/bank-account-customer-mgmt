package com.fdmgroup.dao;

import com.fdmgroup.dto.account.Account;

public interface AccountWriterDAO {

	void deleteAccount(Account account);
	void createAccount(Account account);
}
