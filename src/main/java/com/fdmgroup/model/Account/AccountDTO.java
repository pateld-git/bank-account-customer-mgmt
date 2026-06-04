package com.fdmgroup.model.Account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
	@NotBlank(message = "Type is required")
	@Schema(description = "The category of customer determining the underlying record type.", example = "person", allowableValues = {
			"savings", "checking" })
	private String type;

	@NotNull(message = "Double is required")
	@Schema(description = "The amount of money in the account")
	private Double balance;
	@Schema(description = "The interest rate for savings accounts")
	private Double interestRate;
	@Schema(description = "The next check number for checking account")
	private Integer nextCheckNumber;
}
