package com.fdmgroup.customer.controller;

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

import com.fdmgroup.customer.exception.CityProvinceNotFoundException;
import com.fdmgroup.customer.exception.CustomerNotFoundException;
import com.fdmgroup.customer.exception.GeocoderException;
import com.fdmgroup.customer.exception.PostalCodeNotFoundException;
import com.fdmgroup.customer.model.Customer;
import com.fdmgroup.customer.model.CustomerDTO;
import com.fdmgroup.customer.service.CustomerService;
import com.fdmgroup.customer.service.GeocoderService;

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

	@Operation(summary = "Creates a new customer")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Customer Successfully Created"),
			@ApiResponse(responseCode = "404", description = "No City or Province Found for Given Postal Code")
	})
	@PostMapping
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
		try {
			customerDTO = geocoderService.getPostalCodeProvinceFromGeocoder(customerDTO);
			Customer createdCustomer = customerService.addAccount(customerDTO);

			URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(createdCustomer.getCustomerId()).toUri();

			return ResponseEntity.created(locationUri).body(createdCustomer);
		} catch (GeocoderException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		} catch (CityProvinceNotFoundException | PostalCodeNotFoundException e){
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Retrieves all customers")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200")
	})
	@GetMapping
	public ResponseEntity<List<Customer>> getAllCustomers() {
		return ResponseEntity.ok(customerService.getAllCustomers());
	}

	@Operation(summary = "Retrieves a customer by its ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer Successfully Found"),
			@ApiResponse(responseCode = "404", description = "Customer with Provided ID Not Found")
	})
	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable long customerId) {
		try {
			return ResponseEntity.ok(customerService.getCustomerById(customerId));
		} catch (CustomerNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
	
	@Operation(summary = "Updates a customer by its ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer Successfully Updated"),
			@ApiResponse(responseCode = "404", description = "Customer with provided ID Not Found")
	})
	@PutMapping("/{customerId}")
	public ResponseEntity<Customer> updateCustomerById(@Valid @RequestBody CustomerDTO customerDTO,
			@PathVariable long customerId) {
		try {
			log.info("Updating Customer: " + customerDTO.toString() + "with ID" + customerId);
			return ResponseEntity.ok(customerService.updateById(customerId, customerDTO));
		} catch (CustomerNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
	
	@Operation(summary = "Deletes a customer by its ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer Successfully Deleted"),
			@ApiResponse(responseCode = "404", description = "Customer with Provided ID Not Found")
	})
	@DeleteMapping("/{customerId}")
	public ResponseEntity<Void> deleteById(@PathVariable long customerId) {
		try {
			customerService.deleteById(customerId);
			return ResponseEntity.ok().build();
		} catch (CustomerNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
}
