package com.fdmgroup.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.dao.AccountReaderDAO;
import com.fdmgroup.dao.AccountWriterDAO;
import com.fdmgroup.dto.account.Account;
import com.fdmgroup.dto.account.CheckingAccount;
import com.fdmgroup.dto.account.SavingsAccount;

@ExtendWith(MockitoExtension.class)
class TestAccountService {
	@Mock
	private AccountReaderDAO mockAccountReaderDAO;
	@Mock
	private AccountWriterDAO mockAccountWriterDAO;

	@InjectMocks
	private AccountServiceImpl accountService;

	@Captor
	private ArgumentCaptor<Account> accountCaptor;

	static List<List<Account>> accountListProvider() {
		List<List<Account>> testScenarios = new ArrayList<>();

		testScenarios.add(new ArrayList<>()); // test one

		List<Account> multiAccountList = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			multiAccountList.add(new CheckingAccount());
			multiAccountList.add(new SavingsAccount());
		}
		testScenarios.add(multiAccountList); // test two

		return testScenarios;
	}

	@ParameterizedTest
	@MethodSource("accountListProvider")
	void test_ThatGetAllAccounts_ReturnsListOfAccounts_RecievedFromMockAccountReader(List<Account> expectedAccounts) {

		when(mockAccountReaderDAO.readAccounts()).thenReturn(expectedAccounts);

		List<Account> actualAccounts = accountService.getAllAccounts();

		assertEquals(expectedAccounts.size(), actualAccounts.size());
		verify(mockAccountReaderDAO, times(1)).readAccounts();
	}

	@Test
	void test_WhenRemoveAccountRecievesAnAccount_DeleteAccountIsCalled_InAccountWriterDAO() {
		Account checkToBeDeleted = new CheckingAccount();
		Account saveToBeDeleted = new SavingsAccount();

		accountService.removeAccount(checkToBeDeleted);
		accountService.removeAccount(saveToBeDeleted);

		verify(mockAccountWriterDAO, times(2)).deleteAccount(accountCaptor.capture());
	}

	@Test
	void test_WhenCreateAccountReceiveesAnAccount_CreateAccountFromAccounrWriterDAO_IsCalled() {
		Account newChecking = new CheckingAccount();
		Account newSavings = new SavingsAccount();

		accountService.createAccount(newChecking);
		accountService.createAccount(newSavings);
		
		verify(mockAccountWriterDAO, times(2)).createAccount(accountCaptor.capture());
	}
}
