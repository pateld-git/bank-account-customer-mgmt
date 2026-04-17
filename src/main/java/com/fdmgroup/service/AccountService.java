package com.fdmgroup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fdmgroup.exception.ArgNotFoundException;
import com.fdmgroup.model.Account;
import com.fdmgroup.model.AccountDTO;
import com.fdmgroup.model.CheckingAccount;
import com.fdmgroup.model.SavingsAccount;
import com.fdmgroup.model.customer.Customer;
import com.fdmgroup.repo.AccountRepository;
import com.fdmgroup.repo.CustomerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Validated
@AllArgsConstructor
@Transactional
public class AccountService {
	private AccountRepository accountRepo;
	private CustomerRepository customerRepository;

	public List<Account> getAllAccounts() {
		return accountRepo.findAll();
	}

	public Account getAccountById(long accountId) {
		return accountRepo.findById(accountId)
				.orElseThrow(() -> new ArgNotFoundException("Account not found with ID: " + accountId));
	}

	public List<Account> getAccountsByCity(String city) {
		List<Account> accounts = accountRepo.findAccountsByCustomerCity(city);

		if (accounts.isEmpty()) {
			throw new ArgNotFoundException("No accounts found for customers in city: " + city);
		}

		log.info("Found {} accounts for city: {}", accounts.size(), city);
		return accounts;
	}

	public Account addAccount(long customerId, AccountDTO accountDTO) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ArgNotFoundException("Customer not found with ID: " + customerId));

		Account account;
		if ("savings".equalsIgnoreCase(accountDTO.getType())) {
			account = SavingsAccount.builder()
					.balance(accountDTO.getBalance())
					.interestRate(accountDTO.getInterestRate())
					.customer(customer)
					.build();

		} else if ("checking".equalsIgnoreCase(accountDTO.getType())) {
			account = CheckingAccount.builder()
					.balance(accountDTO.getBalance())
					.nextCheckNumber(accountDTO.getNextCheckNumber() != null ? accountDTO.getNextCheckNumber() : 1)
					.customer(customer)
					.build();
		} else {
			throw new IllegalArgumentException("Account of type: " + accountDTO.getType() + " is invalid.");
		}

		return accountRepo.save(account);
	}

	public Account updateAccount(long accountId, AccountDTO dto) {
		Account existingAccount = accountRepo.findById(accountId)
				.orElseThrow(() -> new ArgNotFoundException("Update failed: Account not found with ID: " + accountId));

		existingAccount.setBalance(dto.getBalance());

		switch (existingAccount) {
		case SavingsAccount savings -> {
			if (dto.getInterestRate() == null) {
				throw new IllegalArgumentException("Update failed: Interest rate is required for Savings accounts.");
			}
			savings.setInterestRate(dto.getInterestRate());
		}
		case CheckingAccount checking -> {
			if (dto.getNextCheckNumber() == null) {
				throw new IllegalArgumentException(
						"Update failed: Next check number is required for Checking accounts.");
			}
			checking.setNextCheckNumber(dto.getNextCheckNumber());
		}
		case null -> throw new IllegalArgumentException("Update failed: Account object is null.");
		default -> throw new IllegalArgumentException("Update failed: Unknown account type for ID: " + accountId);
		}

		return accountRepo.save(existingAccount);
	}

	public void deleteAccount(long accountId) {
		Account account = accountRepo.findById(accountId)
				.orElseThrow(() -> new ArgNotFoundException("Customer not found with provided id: " + accountId));
		accountRepo.delete(account);
	}
}
