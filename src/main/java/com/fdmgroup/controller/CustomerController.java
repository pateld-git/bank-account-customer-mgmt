package com.fdmgroup.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fdmgroup.exception.GeocoderException;
import com.fdmgroup.model.customer.Customer;
import com.fdmgroup.model.customer.CustomerDTO;
import com.fdmgroup.service.CustomerService;
import com.fdmgroup.service.GeocoderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/customers")
@AllArgsConstructor
@Slf4j
public class CustomerController {
	private CustomerService customerService;
	private GeocoderService geocoderService;

	@Operation(summary = "Creates a new customer using postal code geocoding")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Customer successfully created"),
			@ApiResponse(responseCode = "400", description = "Invalid customer data provided"),
			@ApiResponse(responseCode = "404", description = "Postal code could not be resolved to a city/province"),
			@ApiResponse(responseCode = "500", description = "Internal geocoder service failure")
	})
	@PostMapping
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerDTO customerDTO)
			throws GeocoderException {
		customerDTO = geocoderService.getPostalCodeProvinceFromGeocoder(customerDTO);
		Customer createdCustomer = customerService.addCustomer(customerDTO);

		URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(createdCustomer.getCustomerId())
				.toUri();
		return ResponseEntity.created(locationUri).body(createdCustomer);
	}

	@Operation(summary = "Retrieves a list of all registered customers")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list")
	})
	@GetMapping
	public ResponseEntity<List<Customer>> getAllCustomers() {
		return ResponseEntity.ok(customerService.getAllCustomers());
	}

	@Operation(summary = "Retrieves a specific customer by their ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer found"),
			@ApiResponse(responseCode = "404", description = "Customer with provided ID not found")
	})
	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable long customerId) {
		log.info("Finding customer by id: {}", customerId);
		return ResponseEntity.ok(customerService.getCustomerById(customerId));
	}

	@Operation(summary = "Updates an existing customer's details by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer updated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid update data provided"),
			@ApiResponse(responseCode = "404", description = "Customer with provided ID not found")
	})
	@PutMapping("/{customerId}")
	public ResponseEntity<Customer> updateCustomerById(@Valid @RequestBody CustomerDTO customerDTO,
			@PathVariable long customerId) {
		log.info("Updating Customer: {} with ID: {}", customerDTO.getName(), customerId);
		return ResponseEntity.ok(customerService.updateById(customerId, customerDTO));
	}

	@Operation(summary = "Deletes a customer, their address, and all associated accounts by the account ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Customer with provided ID not found")
	})
	@DeleteMapping("/{customerId}")
	public ResponseEntity<Void> deleteById(@PathVariable long customerId) {
		log.info("Deleting customer ID: {}", customerId);
		customerService.deleteById(customerId);
		return ResponseEntity.noContent().build();
	}
}
