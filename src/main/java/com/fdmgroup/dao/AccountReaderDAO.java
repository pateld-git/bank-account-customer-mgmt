package com.fdmgroup.dao;

import java.util.List;

import com.fdmgroup.dto.account.Account;

public interface AccountReaderDAO {

	public List<Account> readAccounts();
}
