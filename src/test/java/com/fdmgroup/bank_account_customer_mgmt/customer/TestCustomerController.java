package com.fdmgroup.bank_account_customer_mgmt.customer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.fdmgroup.controller.CustomerController;
import com.fdmgroup.model.customer.Address;
import com.fdmgroup.model.customer.Company;
import com.fdmgroup.model.customer.Customer;
import com.fdmgroup.model.customer.CustomerDTO;
import com.fdmgroup.model.customer.Person;
import com.fdmgroup.service.CustomerService;
import com.fdmgroup.service.GeocoderService;

@ExtendWith(MockitoExtension.class)
class TestCustomerController {
	private MockMvc mockMvc;

	@Mock
	private CustomerService customerService;

	@Mock
	private GeocoderService geocoderService;

	@InjectMocks
	private CustomerController customerController;

	private ObjectMapper objectMapper;
	private CustomerDTO sampleDTO;
	private Customer person;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		objectMapper = new ObjectMapper();

		Address sampleAddress = Address.builder().streetNumber("123 Street RD").postalCode("86301").build();

		sampleDTO = CustomerDTO.builder().name("John").address(sampleAddress).type("Person").build();

		person = Person.builder().customerId(1L).name("John Doe").address(sampleAddress).accounts(new ArrayList<>())
				.build();
	}

	@Test
	@DisplayName("POST: /api/v1/customers - Handle polymorphism and return Created")
	void createCustomer_Success() throws Exception {
		when(geocoderService.getPostalCodeProvinceFromGeocoder(any(CustomerDTO.class))).thenReturn(sampleDTO);
		when(customerService.addCustomer(any(CustomerDTO.class))).thenReturn(person);

		mockMvc.perform(post("/api/v1/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sampleDTO)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/api/v1/customers/1"))
				.andExpect(jsonPath("$.type").value("person"))
				.andExpect(jsonPath("$.name").value("John Doe"));

		verify(geocoderService, times(1)).getPostalCodeProvinceFromGeocoder(any(CustomerDTO.class));
		verify(customerService, times(1)).addCustomer(any(CustomerDTO.class));
	}

	@Test
	@DisplayName("PUT: /api/v1/customers/{id} - Update and return OK")
	void updateCustomer_Success() throws Exception {
		when(customerService.updateById(eq(1L), any(CustomerDTO.class))).thenReturn(person);

		mockMvc.perform(put("/api/v1/customers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sampleDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.customerId").value(1L))
				.andExpect(jsonPath("$.type").value("person"));

		verify(customerService, times(1)).updateById(eq(1L), any(CustomerDTO.class));
	}

	@Test
	@DisplayName("GET: /api/v1/customers/{id} - Return specific customer")
	void getCustomerById_Success() throws Exception {
		when(customerService.getCustomerById(1L)).thenReturn(person);

		mockMvc.perform(get("/api/v1/customers/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("John Doe"))
				.andExpect(jsonPath("$.type").value("person"));

		verify(customerService, times(1)).getCustomerById(1L);
		;
	}

	@Test
	@DisplayName("DELETE: /api/v1/customers/{id} - Return No Content")
	void deleteCustomer_Success() throws Exception {
		mockMvc.perform(delete("/api/v1/customers/1"))
				.andExpect(status().isNoContent());

		verify(customerService).deleteById(1L);
	}

	@Test
	@DisplayName("GET: /api/v1/customers - Return list of various customer types")
	void testGetAllCustomers_Success() throws Exception {

		Customer company = Company.builder()
				.customerId(2L)
				.name("Tech Corp")
				.build();

		List<Customer> allCustomers = Arrays.asList(person, company);

		when(customerService.getAllCustomers()).thenReturn(allCustomers);

		mockMvc.perform(get("/api/v1/customers")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].customerId").value(1L))
				.andExpect(jsonPath("$[0].type").value("person"))
				.andExpect(jsonPath("$[0].name").value("John Doe"))
				.andExpect(jsonPath("$[1].customerId").value(2L))
				.andExpect(jsonPath("$[1].type").value("company"))
				.andExpect(jsonPath("$[1].name").value("Tech Corp"));

		verify(customerService, times(1)).getAllCustomers();
	}

	@Test
	@DisplayName("GET: /api/v1/customers - Return empty list when no customers exist")
	void testGetAllCustomers_Empty() throws Exception {
		when(customerService.getAllCustomers()).thenReturn(new ArrayList<>());

		mockMvc.perform(get("/api/v1/customers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(0))
				.andExpect(content().json("[]"));
	}
}
