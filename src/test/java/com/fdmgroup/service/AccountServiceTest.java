package com.fdmgroup.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fdmgroup.DAO.AccountReaderDAO;
import com.fdmgroup.DAO.AccountWriterDAO;
import com.fdmgroup.model.Account;
import com.fdmgroup.model.SavingsAccount;

public class AccountServiceTest {
	
	private Account account;
	
	private AccountReaderDAO mockAccountReaderDAO;
	private AccountWriterDAO mockAccountWriterDAO;
	
	private AccountService accountServiceTestAccountService;
	
	@BeforeEach
	public void setUp() {
		account = new SavingsAccount();
		
		mockAccountReaderDAO = mock(AccountReaderDAO.class);
		mockAccountWriterDAO = mock(AccountWriterDAO.class);
		
		accountServiceTestAccountService = new AccountServiceImpl(mockAccountReaderDAO, mockAccountWriterDAO);
	}
	
	@Test
	public void testWhenAccountServiceImpl_callsGetAccounts_andReturnsListofAccounts_fromAccountReaderDAO() {
		// Arrange
		List<Account> expectedAccountList = new ArrayList<>();
		
		//Stubbing
		when(mockAccountReaderDAO.readAccounts()).thenReturn(expectedAccountList);
		
		// Act
		List<Account> actualAccountList = accountServiceTestAccountService.getAccounts();
		
		// Assert
		verify(mockAccountReaderDAO).readAccounts();
		assertSame(expectedAccountList, actualAccountList);
	}
	
	@Test
	public void testWhenAccountServiceImpl_callsRemoveAccount_accountWriterDAO_passingInAction() {
		// Act
		accountServiceTestAccountService.removeAccount(account);
		
		// Assert
		verify(mockAccountWriterDAO).deleteAccount(account);
	}
	
	@Test
	public void testWhenAccountServiceImpl_callsCreateAccount_andPassesAccountObject_intoAccountWriterDAO() {
		// Arrange
		Account expectedAccount = account;
		
		// Stubbing
		when(mockAccountWriterDAO.createAccount(expectedAccount)).thenReturn(expectedAccount);
		
		// Act
		Account actualAccount = accountServiceTestAccountService.createAccount(expectedAccount);
		
		// Assert
		verify(mockAccountWriterDAO).createAccount(expectedAccount);
		assertSame(expectedAccount, actualAccount);
	}
}
