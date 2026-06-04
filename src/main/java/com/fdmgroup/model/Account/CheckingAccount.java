package com.fdmgroup.model.Account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "checking_accounts")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CheckingAccount extends Account {
	@Column(name = "NEXT_CHECK_NUMBER", nullable = false)
	@Schema(description = "The next number of the check from the checkbook")
	private int nextCheckNumber;
}
