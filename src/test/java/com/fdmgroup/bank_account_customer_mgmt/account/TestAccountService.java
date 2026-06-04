package com.fdmgroup.bank_account_customer_mgmt.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.exception.ArgNotFoundException;
import com.fdmgroup.model.Account.Account;
import com.fdmgroup.model.Account.AccountDTO;
import com.fdmgroup.model.Account.CheckingAccount;
import com.fdmgroup.model.Account.SavingsAccount;
import com.fdmgroup.model.customer.Customer;
import com.fdmgroup.model.customer.Person;
import com.fdmgroup.repo.AccountRepository;
import com.fdmgroup.repo.CustomerRepository;
import com.fdmgroup.service.AccountService;

@ExtendWith(MockitoExtension.class)
class TestAccountService {

	@Mock
	private AccountRepository mockAccountRepo;

	@Mock
	private CustomerRepository mockCustomerRepo;

	@InjectMocks
	private AccountService accountService;

	@Test
	void test_addAccount_ReturnsSavings_whenAccountDtoIsValid() {
		long customerId = 1L;
		Customer mockCustomer = new Person();
		AccountDTO dto = AccountDTO.builder().type("savings").balance(500.0).interestRate(2.5).build();

		when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(mockCustomer));
		when(mockAccountRepo.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

		Account result = accountService.addAccount(customerId, dto);

		assertTrue(result instanceof SavingsAccount);
		assertEquals(2.5, ((SavingsAccount) result).getInterestRate());
		assertEquals(mockCustomer, result.getCustomer());
		verify(mockCustomerRepo, times(1)).findById(customerId);
		verify(mockAccountRepo).save(any(SavingsAccount.class));
	}

	@Test
	void test_addAccount_createsChecking_defaultCheckNumber() {
		long customerId = 1L;
		when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(new Person()));
		when(mockAccountRepo.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

		AccountDTO dto = AccountDTO.builder().type("checking").balance(100.0).nextCheckNumber(null).build();

		Account result = accountService.addAccount(customerId, dto);

		assertTrue(result instanceof CheckingAccount);
		assertEquals(1, ((CheckingAccount) result).getNextCheckNumber());
		verify(mockCustomerRepo, times(1)).findById(customerId);
		verify(mockAccountRepo, times(1)).save(any(Account.class));
	}

	@Test
	void test_updateAccount_updatesBalanceAndInterest_andReturnsUpdatedAccount_forSavingsAccount() {
		long accountId = 10L;
		SavingsAccount existing = (SavingsAccount) SavingsAccount.builder().interestRate(1.0).balance(100.0).build();

		AccountDTO updateDto = AccountDTO.builder().balance(200.0).interestRate(3.5).build();

		when(mockAccountRepo.findById(accountId)).thenReturn(Optional.of(existing));
		when(mockAccountRepo.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

		Account result = accountService.updateAccount(accountId, updateDto);

		assertNotNull(result);
		assertTrue(result instanceof SavingsAccount);

		SavingsAccount updatedSavings = (SavingsAccount) result;
		assertEquals(200.0, updatedSavings.getBalance());
		assertEquals(3.5, updatedSavings.getInterestRate());

		verify(mockAccountRepo, times(1)).findById(accountId);
		verify(mockAccountRepo, times(1)).save(any(SavingsAccount.class));

	}

	@Test
	void test_updateAccount_savingsMissingRate_throwsException() {
		long accountId = 10L;
		SavingsAccount existing = new SavingsAccount();
		AccountDTO dto = AccountDTO.builder().balance(100.0).interestRate(null).build();

		when(mockAccountRepo.findById(accountId)).thenReturn(Optional.of(existing));

		assertThrows(IllegalArgumentException.class, () -> accountService.updateAccount(accountId, dto));
		verify(mockAccountRepo, times(1)).findById(accountId);
	}

	@Test
	void test_deleteAccount_success() {
		long id = 1L;
		CheckingAccount account = new CheckingAccount();
		when(mockAccountRepo.findById(id)).thenReturn(Optional.of(account));

		accountService.deleteAccount(id);

		verify(mockAccountRepo, times(1)).delete(account);
	}

	@Test
	void test_getAccountsByCity_empty_throwsException() {
		String city = "Mars";
		when(mockAccountRepo.findAccountsByCustomerCity(city)).thenReturn(java.util.Collections.emptyList());

		assertThrows(ArgNotFoundException.class, () -> accountService.getAccountsByCity(city));
		verify(mockAccountRepo, times(1)).findAccountsByCustomerCity(city);
	}

}
