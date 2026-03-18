package tests.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dao.AccountReaderDAO;
import dao.AccountWriterDAO;
import dto.account.Account;
import dto.account.CheckingAccount;
import dto.account.SavingsAccount;
import service.AccountServiceImpl;

@ExtendWith(MockitoExtension.class)
class TestAccountService { // TODO: Remove comments
	private static final Logger LOGGER = LogManager.getLogger(TestAccountService.class);

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
		LOGGER.info("test_ThatGetAllAccounts_ReturnsListOfAccounts_RecievedFromMockAccountReader: {}",
				expectedAccounts);

		when(mockAccountReaderDAO.readAccounts()).thenReturn(expectedAccounts);

		List<Account> actualAccounts = accountService.getAllAccounts();
		LOGGER.debug("Received {} accounts from service", actualAccounts.size());

		try {
			assertEquals(expectedAccounts.size(), actualAccounts.size());
			verify(mockAccountReaderDAO, times(1)).readAccounts();
			LOGGER.debug("Assertions Successful");
		} catch (AssertionError e) {
			LOGGER.error("Test failed Expected: {}, Actual: {}", expectedAccounts.size(), actualAccounts.size());
			throw e;
		}
	}

	@Test
	void test_WhenRemoveAccountRecievesAnAccount_DeleteAccountIsCalled_InAccountWriterDAO() {
		LOGGER.info("test_WhenRemoveAccountRecievesAnAccount_DeleteAccountIsCalled_InAccountWriterDAO");

		Account checkToBeDeleted = new CheckingAccount();
		Account saveToBeDeleted = new SavingsAccount();

		accountService.removeAccount(checkToBeDeleted);
		accountService.removeAccount(saveToBeDeleted);

		try {
			verify(mockAccountWriterDAO, times(2)).deleteAccount(accountCaptor.capture());

			List<Account> capturedAccounts = accountCaptor.getAllValues();
			LOGGER.debug("VERIFY SUCCESS: Captured {} and {} deletion calls.", capturedAccounts.get(0),
					capturedAccounts.get(1));

		} catch (AssertionError e) {
			LOGGER.error("VERIFY FAILURE: Account deletion sequence interrupted.");
			throw e;
		}
	}

	@Test
	void test_WhenCreateAccountReceiveesAnAccount_CreateAccountFromAccounrWriterDAO_IsCalled() {
		LOGGER.info("test_WhenCreateAccountReceiveesAnAccount_CreateAccountFromAccounrWriterDAO_IsCalled");

		Account newChecking = new CheckingAccount();
		Account newSavings = new SavingsAccount();

		accountService.createAccount(newChecking);
		accountService.createAccount(newSavings);

		try {
			verify(mockAccountWriterDAO, times(2)).createAccount(accountCaptor.capture());

			List<Account> capturedAccounts = accountCaptor.getAllValues();
			LOGGER.debug("VERIFY SUCCESS: Captured {} and {} creation calls.", capturedAccounts.get(0),
					capturedAccounts.get(1));

		} catch (AssertionError e) {
			LOGGER.error("VERIFY FAILURE: Account creation sequence interrupted.");
			throw e;
		}
	}
}
