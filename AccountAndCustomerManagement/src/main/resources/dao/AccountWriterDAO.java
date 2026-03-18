package dao;

import dto.account.Account;

public interface AccountWriterDAO {

	void deleteAccount(Account account);
	void createAccount(Account account);
}
