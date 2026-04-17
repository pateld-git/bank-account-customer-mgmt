package com.fdmgroup.model.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Data Transfer Object representing a Customer and their location details")
public class CustomerDTO {
	@NotBlank(message = "Type is required")
	@Schema(
	    description = "The category of customer determining the underlying record type.", 
	    example = "person", 
	    allowableValues = {"person", "company"}
	)
	private String type;
	
	@NotBlank(message = "Name is required")
	@Schema(description = "The full name of the customer", example = "John Doe")
	private String name;
	
	@NotNull(message = "Address object must be provided")
	@Valid
	@Schema(description = 
			"The customer's address information. Note: City and Province are populated automatically via Geocoder.")
	private Address address;
}
