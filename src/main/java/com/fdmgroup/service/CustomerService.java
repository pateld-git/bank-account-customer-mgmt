package com.fdmgroup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fdmgroup.exception.ArgNotFoundException;
import com.fdmgroup.model.address.Address;
import com.fdmgroup.model.customer.Company;
import com.fdmgroup.model.customer.Customer;
import com.fdmgroup.model.customer.CustomerDTO;
import com.fdmgroup.model.customer.Person;
import com.fdmgroup.repo.CustomerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@AllArgsConstructor
@Slf4j
@Transactional
public class CustomerService {
	private final CustomerRepository customerRepo;

	public List<Customer> getAllCustomers() {
		log.info("Fetching all customers");
		return customerRepo.findAll();
	}

	public Customer addCustomer(CustomerDTO dto) {
		log.info("Creating new customer account for: {} with type: {}", dto.getName(), dto.getType());

		Customer newCustomer;

		if ("company".equalsIgnoreCase(dto.getType())) {
			newCustomer = Company.builder()
					.name(dto.getName())
					.address(dto.getAddress())
					.build();
		} else if ("person".equalsIgnoreCase(dto.getType())) {
			newCustomer = Person.builder()
					.name(dto.getName())
					.address(dto.getAddress())
					.build();
		} else {
			throw new ArgNotFoundException("Invalid customer type: " + dto.getType());
		}

		if (newCustomer.getAddress() != null) {
			newCustomer.getAddress().setCustomer(newCustomer);
		}

		return customerRepo.save(newCustomer);
	}

	public Customer getCustomerById(long customerId) {
		log.info("Finding a customer with id: {}", customerId);
		return customerRepo.findById(customerId)
				.orElseThrow(() -> new ArgNotFoundException("Customer not found with provided id: " + customerId));
	}

	public Customer updateById(long customerId, CustomerDTO dto) throws ArgNotFoundException {
		Customer existingCustomer = customerRepo.findById(customerId)
				.orElseThrow(() -> new ArgNotFoundException("Customer not found with id: " + customerId));

		log.info("Found customer with id: {}", customerId);
		existingCustomer.setName(dto.getName());

		if (dto.getAddress() != null && existingCustomer.getAddress() != null) {
			Address incomingData = dto.getAddress();

			existingCustomer.getAddress()
					.setStreetNumber(incomingData.getStreetNumber())
					.setCity(incomingData.getCity())
					.setProvince(incomingData.getProvince())
					.setPostalCode(incomingData.getPostalCode());
		}

		return customerRepo.save(existingCustomer);
	}

	public void deleteById(long customerId) {
		log.info("Deleting customer with id: {}", customerId);
		Customer customer = customerRepo.findById(customerId)
				.orElseThrow(() -> new ArgNotFoundException("Customer not found with provided id: " + customerId));
		customerRepo.delete(customer);
	}

}
