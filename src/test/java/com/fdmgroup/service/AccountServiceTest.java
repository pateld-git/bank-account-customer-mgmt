package com.fdmgroup.service;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fdmgroup.DAO.AccountReaderDAO;
import com.fdmgroup.DAO.AccountWriterDAO;

public class AccountServiceTest {
	
	private AccountReaderDAO mockAccountReaderDAO;
	private AccountWriterDAO mockAccountWriterDAO;
	private AccountService accountServiceTestAccountService;
	
	@BeforeEach
	public void setUp() {
		accountServiceTestAccountService = new AccountServiceImpl();
		mockAccountReaderDAO = mock(AccountReaderDAO.class);
		mockAccountWriterDAO = mock(AccountWriterDAO.class);
	}
	
	@Test
	public void test1() {
		// Arrange
		
		
		// Act
		
		
		// Assert
		
		
	}
}
