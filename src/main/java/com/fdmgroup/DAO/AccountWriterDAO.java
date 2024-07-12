package com.fdmgroup.DAO;

import com.fdmgroup.model.Account;

public interface AccountWriterDAO {
	
	public Account createAccount(Account account);
	public void deleteAccount(Account account);
}
