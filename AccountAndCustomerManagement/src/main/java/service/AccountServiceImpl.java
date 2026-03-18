package service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.AccountReaderDAO;
import dao.AccountWriterDAO;
import dto.account.Account;

public class AccountServiceImpl implements AccountService {
	private static final Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);
	private AccountReaderDAO accountReaderDAO;
	private AccountWriterDAO accountWriterDAO;

	public AccountServiceImpl(AccountReaderDAO accountReaderDAO, AccountWriterDAO accountWriterDAO) {
		super();
		this.accountReaderDAO = accountReaderDAO;
		this.accountWriterDAO = accountWriterDAO;
	}

	@Override
	public List<Account> getAllAccounts() {
		LOGGER.debug("AccountServiceImpl: getAllAccounts");
		return accountReaderDAO.readAccounts();
	}
	
	@Override
	public void removeAccount(Account deleteAccount) {
		LOGGER.debug("AccountServiceImpl: RemoveAccount - {}", deleteAccount);
		accountWriterDAO.deleteAccount(deleteAccount);
	}
	
	@Override
	public void createAccount(Account newAccount) {
		LOGGER.debug("AccountServiceImpl: createAccount - {}", newAccount);
		accountWriterDAO.createAccount(newAccount);
	}

}
