package com.fdmgroup.address.model;

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
	private String type;
	@NotNull(message = "Double is required")
    private Double balance;
    private Double interestRate;
    private Integer nextCheckNumber;
}
