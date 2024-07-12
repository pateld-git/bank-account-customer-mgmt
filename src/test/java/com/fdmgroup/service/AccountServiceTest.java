package com.fdmgroup.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.fdmgroup.DAO.AccountReaderDAO;
import com.fdmgroup.DAO.AccountWriterDAO;
import com.fdmgroup.model.Account;

public class AccountServiceTest {
	
	private AccountReaderDAO mockAccountReaderDAO;
	private AccountWriterDAO mockAccountWriterDAO;
	
	private AccountService accountServiceTestAccountService;
	
	@BeforeEach
	public void setUp() {
		mockAccountReaderDAO = mock(AccountReaderDAO.class);
		mockAccountWriterDAO = mock(AccountWriterDAO.class);
		accountServiceTestAccountService = new AccountServiceImpl(mockAccountReaderDAO, mockAccountWriterDAO);
	}
	
	@Test
	public void test1() {
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
}
