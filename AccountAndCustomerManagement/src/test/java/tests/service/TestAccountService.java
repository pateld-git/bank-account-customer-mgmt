package tests.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import dao.AccountReaderDAO;
import dao.AccountWriterDAO;
import service.AccountService;
import service.AccountServiceImpl;

class TestAccountService {
	private AccountService accountService = new AccountServiceImpl();
	
	@Mock
	private AccountReaderDAO accountReaderDAO;
	@Mock
	private AccountWriterDAO accountWriterDAO;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test_GetAllAccounts_ReturnsEmptyAccountList_IfNoAccountsInDatabase() {
		
		fail("Not yet implemented");
	}

}
