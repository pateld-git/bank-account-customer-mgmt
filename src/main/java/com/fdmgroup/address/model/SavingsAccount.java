package com.fdmgroup.address.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SAVINGS_ACCOUNT")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class SavingsAccount extends Account {
	
	@Column(name = "INTEREST_RATE", nullable = false)
	private double interestRate;
}
