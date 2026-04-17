package com.fdmgroup.address.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "checking_accounts")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class CheckingAccount extends Account {
	@Column(name = "NEXT_CHECK_NUMBER", nullable = false)
    private int nextCheckNumber;
}
