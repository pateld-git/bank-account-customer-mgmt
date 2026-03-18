package dao;

import java.util.List;

import dto.account.Account;

public interface AccountReaderDAO {

	public List<Account> readAccounts();
}
