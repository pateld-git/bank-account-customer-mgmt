package com.fdmgroup.customer.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.customer.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
}
