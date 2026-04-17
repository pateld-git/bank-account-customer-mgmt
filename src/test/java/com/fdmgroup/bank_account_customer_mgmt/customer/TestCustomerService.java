package com.fdmgroup.bank_account_customer_mgmt.customer;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.exception.ArgNotFoundException;
import com.fdmgroup.model.customer.Address;
import com.fdmgroup.model.customer.Company;
import com.fdmgroup.model.customer.Customer;
import com.fdmgroup.model.customer.CustomerDTO;
import com.fdmgroup.model.customer.Person;
import com.fdmgroup.repo.CustomerRepository;
import com.fdmgroup.service.CustomerService;

@ExtendWith(MockitoExtension.class)
class TestCustomerService {

	@Mock
	private CustomerRepository mockCustomerRepo;

	@InjectMocks
	private CustomerService customerService;

	@Test
	void test_getAllCustomers_returnsListOfCustomers() {
		when(mockCustomerRepo.findAll()).thenReturn(List.of(new Person(), new Company()));

		List<Customer> result = customerService.getAllCustomers();

		assertEquals(2, result.size());
		verify(mockCustomerRepo, times(1)).findAll();
	}

	@Test
	void test_addCustomer_createsCompany() {
		CustomerDTO dto = CustomerDTO.builder().name("Tech Corp").type("company")
				.address(Address.builder().streetNumber("10").build()).build();

		when(mockCustomerRepo.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);

		Customer result = customerService.addCustomer(dto);

		assertTrue(result instanceof Company);
		assertEquals("Tech Corp", result.getName());
		assertEquals(result, result.getAddress().getCustomer());
		verify(mockCustomerRepo, times(1)).save(any(Company.class));
	}

	@Test
	void test_addCustomer_createsPerson() {
		CustomerDTO dto = CustomerDTO.builder().name("Alice").type("person")
				.address(Address.builder().streetNumber("10").build()).build();

		when(mockCustomerRepo.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);

		Customer result = customerService.addCustomer(dto);

		assertTrue(result instanceof Person);
		assertEquals("Alice", result.getName());
		assertEquals(result, result.getAddress().getCustomer());
		verify(mockCustomerRepo, times(1)).save(any(Person.class));
	}

	@Test
	void test_getCustomerById_throwsException_whenCustomerId_isNotExistInRepo() {
		when(mockCustomerRepo.findById(99L)).thenReturn(Optional.empty());

		assertThrows(ArgNotFoundException.class, () -> customerService.getCustomerById(99L));
	}

	@Test
	void test_updateById_updatesNameAndAddressFields_andReturnsUpdatedCustomer() {
		long id = 1L;

		Address existingAddress = new Address().setAddressId(100L).setStreetNumber("123 Old St").setCity("Old City");

		Customer existingCustomer = new Person().setCustomerId(id).setName("Original Name").setAddress(existingAddress);

		Address incomingAddressData = Address.builder().streetNumber("456 New Ave").city("New City").province("ON")
				.postalCode("M1M1M1").build();

		CustomerDTO updateDto = CustomerDTO.builder().name("Updated Name").address(incomingAddressData).build();

		when(mockCustomerRepo.findById(id)).thenReturn(Optional.of(existingCustomer));
		when(mockCustomerRepo.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);

		Customer result = customerService.updateById(id, updateDto);

		assertNotNull(result);
		assertEquals("Updated Name", result.getName());
		assertEquals("456 New Ave", result.getAddress().getStreetNumber());
		assertEquals("New City", result.getAddress().getCity());
		assertEquals("ON", result.getAddress().getProvince());

		assertEquals(id, result.getCustomerId());

		verify(mockCustomerRepo, times(1)).findById(id);
		verify(mockCustomerRepo, times(1)).save(existingCustomer);

	}

	@Test
	void test_deleteById_success() {
		long id = 1L;
		Customer person = new Person();
		when(mockCustomerRepo.findById(id)).thenReturn(Optional.of(person));

		customerService.deleteById(id);

		verify(mockCustomerRepo, times(1)).delete(person);
	}
}
