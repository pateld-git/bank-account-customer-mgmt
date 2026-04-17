package com.fdmgroup.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.model.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
}
