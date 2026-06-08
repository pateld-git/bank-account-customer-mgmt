package com.fdmgroup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fdmgroup.exception.ArgNotFoundException;
import com.fdmgroup.model.account.Account;
import com.fdmgroup.model.account.AccountDTO;
import com.fdmgroup.model.account.CheckingAccount;
import com.fdmgroup.model.account.SavingsAccount;
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
		log.info("Fetching all accounts");
		return accountRepo.findAll();
	}

	public Account getAccountById(long accountId) {
		log.info("Fetching account with id {}", accountId);
		return accountRepo.findById(accountId)
				.orElseThrow(() -> new ArgNotFoundException("Account not found with ID: " + accountId));
	}

	public List<Account> getAccountsByCustomerId(long customerId) {
		log.info("Fetching accounts from customer with id: ", customerId);
		List<Account> accounts = accountRepo.findAccountsByCustomerId(customerId);

		if (accounts.isEmpty()) {
			throw new ArgNotFoundException("No accounts found for customer with id: " + customerId);
		}

		log.info("Found {} accounts from account with id: {}", accounts.size(), customerId);
		return accounts;
	}

	public List<Account> getAccountsByCity(String city) {
		log.info("Fetching accounts of customers from city {}", city);
		List<Account> accounts = accountRepo.findAccountsByCustomerCity(city);

		if (accounts.isEmpty()) {
			throw new ArgNotFoundException("No accounts found for customers in city: " + city);
		}

		log.info("Found {} accounts for city: {}", accounts.size(), city);
		return accounts;
	}

	public Account addAccount(long customerId, AccountDTO accountDTO) {
		log.info("Checking if customer with ID {} exists", customerId);
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ArgNotFoundException("Customer not found with ID: " + customerId));

		log.info("Creating new {} account", accountDTO.getType());

		Account account;
		if ("savings".equalsIgnoreCase(accountDTO.getType())) {
			account = SavingsAccount.builder()
					.balance(accountDTO.getBalance())
					.interestRate(accountDTO.getInterestRate() != null ? accountDTO.getInterestRate() : 1.5)
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
		log.info("Checking if account with ID {} exists", accountId);
		Account existingAccount = accountRepo.findById(accountId)
				.orElseThrow(() -> new ArgNotFoundException("Update failed: Account not found with ID: " + accountId));

		log.info("Updating account {}", accountId);

		existingAccount.setBalance(dto.getBalance());

		switch (existingAccount) {
			case SavingsAccount savings -> {
				if (dto.getInterestRate() == null) {
					throw new IllegalArgumentException(
							"Update failed: Interest rate is required for Savings accounts.");
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
		log.info("Deleting account {}", accountId);
		Account account = accountRepo.findById(accountId)
				.orElseThrow(() -> new ArgNotFoundException("Customer not found with provided id: " + accountId));
		accountRepo.delete(account);
	}
}
