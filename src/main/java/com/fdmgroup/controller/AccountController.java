package com.fdmgroup.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.model.Account.Account;
import com.fdmgroup.model.Account.AccountDTO;
import com.fdmgroup.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/accounts")
@AllArgsConstructor
@Slf4j
public class AccountController {
	private AccountService accountService;

	@Operation(summary = "Retrieves a list of all registered accounts")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list")
	})
	@GetMapping
	public ResponseEntity<List<Account>> getAllAccounts() {
		return ResponseEntity.ok(accountService.getAllAccounts());
	}

	@Operation(summary = "Retrieves accounts from a specific user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Account found"),
			@ApiResponse(responseCode = "404", description = "No accounts registered to customers in specified city")
	})
	@GetMapping("/search-customer")
	public ResponseEntity<List<Account>> getAccountsByCustomerId(@RequestParam long customerId) {
		return ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId));
	}

	@Operation(summary = "Retrieves accounts from a user from a specific city")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Account found"),
			@ApiResponse(responseCode = "404", description = "No accounts registered to customers in specified city")
	})
	@GetMapping("/search-city")
	public ResponseEntity<List<Account>> getAccountsByCity(@RequestParam String city) {
		return ResponseEntity.ok(accountService.getAccountsByCity(city));
	}

	@Operation(summary = "Retrieves a specific account by their ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Account found"),
			@ApiResponse(responseCode = "404", description = "Account with provided ID not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable("id") long accountId) {
		return ResponseEntity.ok(accountService.getAccountById(accountId));
	}

	@Operation(summary = "Creates a new account linked to an existing customer")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Account successfully created"),
			@ApiResponse(responseCode = "400", description = "Invalid account data provided"),
			@ApiResponse(responseCode = "404", description = "Customer not found with provided ID"),
	})
	@PostMapping("/customer/{customerId}")
	public ResponseEntity<Account> createAccount(@PathVariable long customerId,
			@Valid @RequestBody AccountDTO accountDto) {

		Account createdAccount = accountService.addAccount(customerId, accountDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
	}

	@Operation(summary = "Updates an existing account's details by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Account updated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid update data provided"),
			@ApiResponse(responseCode = "404", description = "Account with provided ID not found")
	})
	@PutMapping("/{id}")
	public ResponseEntity<Account> updateAccount(@PathVariable("id") long accountId,
			@Valid @RequestBody AccountDTO accountDto) {
		Account updatedAccount = accountService.updateAccount(accountId, accountDto);
		return ResponseEntity.ok(updatedAccount);
	}

	@Operation(summary = "Deletes an account by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Account deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Account with provided ID not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAccount(@PathVariable("id") long accountId) {
		accountService.deleteAccount(accountId);
		return ResponseEntity.noContent().build();
	}
}
