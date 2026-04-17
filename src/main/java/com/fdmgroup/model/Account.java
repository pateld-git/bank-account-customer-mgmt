package com.fdmgroup.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fdmgroup.model.customer.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = SavingsAccount.class, name = "SAVINGS"),
		@JsonSubTypes.Type(value = CheckingAccount.class, name = "CHECKING")
})
public abstract class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Unique identifier of the account")
	@Column(name = "ACCOUNT_ID")
	private long accountId;

	@Column(name = "BALANCE", nullable = false)
	@Schema(description = "Current balance in the account")
	private double balance;

	@ManyToOne
	@JoinColumn(name = "FK_CUST_ID", nullable = false)
	@Schema(description = "The customer who owns this account")
	@JsonBackReference
	private Customer customer;
}
