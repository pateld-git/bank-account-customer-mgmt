package com.fdmgroup.address.model;

import com.fdmgroup.customer.model.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Creates one table with a 'type' column
@DiscriminatorColumn(name = "account_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Account {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Long accountId;
    
	@Column(name = "BALANCE", nullable = false)
    private double balance;

    @ManyToOne
    @JoinColumn(name = "FK_CUST_ID")
    private Customer customer;
}
