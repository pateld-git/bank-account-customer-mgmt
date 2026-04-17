package com.fdmgroup.customer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fdmgroup.customer.exception.CustomerNotFoundException;
import com.fdmgroup.customer.model.Address;
import com.fdmgroup.customer.model.Company;
import com.fdmgroup.customer.model.Customer;
import com.fdmgroup.customer.model.CustomerDTO;
import com.fdmgroup.customer.model.Person;
import com.fdmgroup.customer.repo.CustomerRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@AllArgsConstructor
@Slf4j
public class CustomerService {
	private CustomerRepository customerRepo;

	public List<Customer> getAllCustomers() {
		return customerRepo.findAll();
	}

	public Customer addAccount(CustomerDTO dto) {
	    log.info("Creating new customer account for: {} with type: {}", dto.getName(), dto.getType());

	    Customer newCustomer;

	    if ("company".equalsIgnoreCase(dto.getType())) {
	        newCustomer = Company.builder()
	                .name(dto.getName())
	                .address(dto.getAddress())
	                .build();
	    } else {
	        newCustomer = Person.builder()
	                .name(dto.getName())
	                .address(dto.getAddress())
	                .build();
	    }

	    if (newCustomer.getAddress() != null) {
	        newCustomer.getAddress().setCustomer(newCustomer);
	    }
	    
	    return customerRepo.save(newCustomer);
	}
	
	public Customer getCustomerById(long customerId) {
		return customerRepo.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with provided id: " + customerId));
	}

	public Customer updateById(long customerId, @Valid CustomerDTO dto) throws CustomerNotFoundException {
	    Customer existingCustomer = customerRepo.findById(customerId)
	            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

	    existingCustomer.setName(dto.getName());
	    
	    if (dto.getAddress() != null) {
	        Address addressToUpdate = existingCustomer.getAddress();
	        Address incomingData = dto.getAddress();
	        
	        addressToUpdate.setStreetNumber(incomingData.getStreetNumber());
	        addressToUpdate.setPostalCode(incomingData.getPostalCode());
	        addressToUpdate.setCity(incomingData.getCity());
	        addressToUpdate.setProvince(incomingData.getProvince());
	    }

	    return customerRepo.save(existingCustomer);
	}

	public void deleteById(long customerId) {
		if (!customerRepo.existsById(customerId)) {
			throw new CustomerNotFoundException("Customer not found with provided id: " + customerId);
		}

		customerRepo.deleteById(customerId);
	}

}
