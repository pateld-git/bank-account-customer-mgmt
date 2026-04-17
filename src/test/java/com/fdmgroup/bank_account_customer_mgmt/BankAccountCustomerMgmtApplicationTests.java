package com.fdmgroup.bank_account_customer_mgmt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
class BankAccountCustomerMgmtApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private WebClient geocoderWebClient;

	@Test
	void contextLoads() {
		assertNotNull(context, "The application context should not be null");
	}

	@Test
	void geocoderWebClientBeanExists() {
		assertNotNull(geocoderWebClient, "The geocoderWebClient bean should be registered in the context");
	}

}
