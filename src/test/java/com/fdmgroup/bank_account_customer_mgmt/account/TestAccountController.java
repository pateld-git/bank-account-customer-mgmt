package com.fdmgroup.bank_account_customer_mgmt.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.controller.AccountController;
import com.fdmgroup.model.Account.Account;
import com.fdmgroup.model.Account.AccountDTO;
import com.fdmgroup.model.Account.CheckingAccount;
import com.fdmgroup.model.Account.SavingsAccount;
import com.fdmgroup.service.AccountService;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

	private MockMvc mockMvc;

	@Mock
	private AccountService accountService;

	@InjectMocks
	private AccountController accountController;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	@DisplayName("POST: /api/accounts/customer/{id} - Create and Return Savings")
	void createSavingsAccount_Success() throws Exception {
		AccountDTO savingsDTO = AccountDTO.builder().type("savings").balance(500.0).interestRate(2.5).build();

		Account savingsAccount = SavingsAccount.builder().accountId(1L).balance(500.0).interestRate(2.5).build();

		when(accountService.addAccount(eq(10L), eq(savingsDTO))).thenReturn(savingsAccount);

		mockMvc.perform(post("/api/accounts/customer/10")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(savingsDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.type").value("SAVINGS"))
				.andExpect(jsonPath("$.balance").value(500.0))
				.andExpect(jsonPath("$.interestRate").value(2.5));

		verify(accountService).addAccount(eq(10L), any(AccountDTO.class));
	}

	@Test
	@DisplayName("POST: /api/accounts/customer/{id} - Missing Balance")
	void createAccount_ValidationError() throws Exception {
		AccountDTO invalidDTO = AccountDTO.builder().type("checking").balance(null).build();

		mockMvc.perform(post("/api/accounts/customer/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidDTO)))
				.andExpect(status().isBadRequest());

		verifyNoInteractions(accountService);
	}

	@Test
	@DisplayName("GET: /api/accounts/search - Return accounts of customers within a city")
	void searchAccountsByCity_Success() throws Exception {
		Account savings = SavingsAccount.builder().accountId(1L).balance(100.0).build();
		Account checking = CheckingAccount.builder().accountId(2L).balance(200.0).build();

		when(accountService.getAccountsByCity("Toronto")).thenReturn(Arrays.asList(savings, checking));

		mockMvc.perform(get("/api/accounts/search-city")
				.param("city", "Toronto"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].type").value("SAVINGS"))
				.andExpect(jsonPath("$[1].type").value("CHECKING"));

		verify(accountService, times(1)).getAccountsByCity("Toronto");
	}

	@Test
	@DisplayName("GET /api/accounts - Should return list with polymorphic types")
	void testGetAllAccounts_Success() throws Exception {
		// Arrange
		Account savings = SavingsAccount.builder().accountId(1L).balance(100.0).interestRate(2.1).build();

		Account checking = CheckingAccount.builder().accountId(2L).balance(200.0).nextCheckNumber(500).build();

		when(accountService.getAllAccounts()).thenReturn(Arrays.asList(savings, checking));

		mockMvc.perform(get("/api/accounts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].type").value("SAVINGS"))
				.andExpect(jsonPath("$[0].accountId").value(1L))
				.andExpect(jsonPath("$[0].interestRate").value(2.1))
				.andExpect(jsonPath("$[1].type").value("CHECKING"))
				.andExpect(jsonPath("$[1].accountId").value(2L))
				.andExpect(jsonPath("$[1].nextCheckNumber").value(500));

		verify(accountService, times(1)).getAllAccounts();
	}

	@Test
	@DisplayName("GET: /api/accounts/{id} - Return specific account")
	void testGetAccountById_Success() throws Exception {
		// Arrange
		Account checking = CheckingAccount.builder()
				.accountId(99L)
				.balance(50.0)
				.nextCheckNumber(1001)
				.build();

		when(accountService.getAccountById(99L)).thenReturn(checking);

		mockMvc.perform(get("/api/accounts/99"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accountId").value(99L))
				.andExpect(jsonPath("$.type").value("CHECKING"))
				.andExpect(jsonPath("$.nextCheckNumber").value(1001));

		verify(accountService, times(1)).getAccountById(99L);
	}

	@Test
	@DisplayName("PUT: /api/accounts/{id} - Update Checking")
	void updateAccount_Success() throws Exception {
		AccountDTO updateDTO = AccountDTO.builder().type("checking").balance(1500.0).nextCheckNumber(101).build();

		Account updatedChecking = CheckingAccount.builder().accountId(5L).balance(1500.0).nextCheckNumber(101).build();

		when(accountService.updateAccount(eq(5L), any(AccountDTO.class))).thenReturn(updatedChecking);

		mockMvc.perform(put("/api/accounts/5")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nextCheckNumber").value(101))
				.andExpect(jsonPath("$.type").value("CHECKING"));

		verify(accountService, times(1)).updateAccount(eq(5L), any(AccountDTO.class));
	}

	@Test
	@DisplayName("DELETE: /api/accounts/{id} - Return 204 No Content")
	void testDeleteAccount_Success() throws Exception {
		long accountId = 123L;
		doNothing().when(accountService).deleteAccount(accountId);

		mockMvc.perform(delete("/api/accounts/{id}", accountId))
				.andExpect(status().isNoContent());

		verify(accountService, times(1)).deleteAccount(accountId);
	}
}
