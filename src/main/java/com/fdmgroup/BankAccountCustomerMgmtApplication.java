package com.fdmgroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class BankAccountCustomerMgmtApplication {
	private final String GEOCODER_BASEURL = "https://geocoder.ca";

	public static void main(String[] args) {
		SpringApplication.run(BankAccountCustomerMgmtApplication.class, args);
	}
	
	@Bean
	WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}
	
	@Bean
	WebClient geocoderWebClient(WebClient.Builder builder) {
		return builder.baseUrl(GEOCODER_BASEURL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

}
