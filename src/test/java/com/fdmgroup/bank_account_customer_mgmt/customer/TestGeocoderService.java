package com.fdmgroup.bank_account_customer_mgmt.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.fdmgroup.exception.ArgNotFoundException;
import com.fdmgroup.exception.GeocoderException;
import com.fdmgroup.model.customer.Address;
import com.fdmgroup.model.customer.CustomerDTO;
import com.fdmgroup.service.GeocoderService;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

class TestGeocoderService {
	private static MockWebServer mockBackEnd;
	private GeocoderService geocoderService;

	@BeforeAll
	static void setUp() throws IOException {
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}

	@BeforeEach
	void initialize() {
		String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
		WebClient webClient = WebClient.create(baseUrl);
		geocoderService = new GeocoderService(webClient);
	}

	@Test
	@DisplayName("1. Should update address and preserve existing data when API returns valid data")
	void test_validPostalCodeInput_ReturnsCityAndProvince() throws Exception {
		String mockJsonResponse = """
				{
				    "standard": {
				        "city": "Toronto",
				        "prov": "ON"
				    }
				}
				""";

		mockBackEnd.enqueue(new MockResponse()
				.setBody(mockJsonResponse)
				.addHeader("Content-Type", "application/json"));

		Address initialAddress = Address.builder()
				.postalCode("M5V2H1")
				.streetNumber("123 Fake St")
				.build();

		CustomerDTO inputDto = CustomerDTO.builder()
				.name("John")
				.address(initialAddress)
				.build();

		CustomerDTO result = geocoderService.getPostalCodeProvinceFromGeocoder(inputDto);

		assertNotNull(result);
		assertEquals("Toronto", result.getAddress().getCity());
		assertEquals("ON", result.getAddress().getProvince());

		assertEquals("John", result.getName());
		assertEquals("123 Fake St", result.getAddress().getStreetNumber());
		assertEquals("M5V2H1", result.getAddress().getPostalCode());

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals("GET", recordedRequest.getMethod());
		assertTrue(recordedRequest.getPath().contains("locate=M5V2H1"));
		assertTrue(recordedRequest.getPath().contains("json=1"));
	}

	@Test
	@DisplayName("2. 5xx ERROR: Should throw GeocoderException when API server returns 500")
	void test_throwGeocoderExceptionOn500() throws InterruptedException {
		mockBackEnd.enqueue(new MockResponse().setResponseCode(500));

		CustomerDTO inputDto = CustomerDTO.builder()
				.address(Address.builder().postalCode("M5V2H1").build()).build();

		GeocoderException ex = assertThrows(GeocoderException.class, () -> {
			geocoderService.getPostalCodeProvinceFromGeocoder(inputDto);
		});

		assertTrue(ex.getMessage().contains("External API error"));
		assertNotNull(mockBackEnd.takeRequest());
	}

	@Test
	@DisplayName("3. 4xx ERROR: Should throw PostalCodeNotFoundException when API returns 404")
	void test_throwPostalCodeNotFoundExceptionOn404() throws InterruptedException {
		mockBackEnd.enqueue(new MockResponse().setResponseCode(404));

		CustomerDTO inputDto = CustomerDTO.builder()
				.address(Address.builder().postalCode("BAD-ZIP").build()).build();

		ArgNotFoundException ex = assertThrows(ArgNotFoundException.class, () -> {
			geocoderService.getPostalCodeProvinceFromGeocoder(inputDto);
		});

		assertTrue(ex.getMessage().contains("postal code not found"));
		assertNotNull(mockBackEnd.takeRequest());
	}

	@Test
	@DisplayName("4. DATA ERROR: Should throw CityProvinceNotFoundException when JSON body is missing 'standard' data")
	void test_throwCityProvinceNotFoundExceptionWhenDataIsMissing() throws InterruptedException {
		String emptyJsonResponse = "{\"standard\": null}";
		mockBackEnd.enqueue(new MockResponse()
				.setBody(emptyJsonResponse)
				.addHeader("Content-Type", "application/json"));

		CustomerDTO inputDto = CustomerDTO.builder()
				.address(Address.builder().postalCode("NULL-DATA").build()).build();

		ArgNotFoundException ex = assertThrows(ArgNotFoundException.class, () -> {
			geocoderService.getPostalCodeProvinceFromGeocoder(inputDto);
		});

		assertTrue(ex.getMessage().contains("No valid geocode data found"));
		assertNotNull(mockBackEnd.takeRequest());
	}
}
