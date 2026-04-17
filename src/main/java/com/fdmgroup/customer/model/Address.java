package com.fdmgroup.customer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Represents a physical address, Geocoder populates City and Province using Postal Code")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Unique identifier of the address")
	private Long addressId;

	@NotBlank(message = "Street Number is required.")
	@Schema(example = "123", description = "Street number of the location")
	private String streetNumber;

	@Schema(description = "City name - Automatically populated from postal code", 
	        accessMode = Schema.AccessMode.READ_ONLY, example = "Toronto")
	private String city;

	@Schema(description = "Province code - Automatically populated from postal code", 
	        accessMode = Schema.AccessMode.READ_ONLY, example = "ON")
	private String province;

	@NotBlank(message = "Postal Code is required")
	@Schema(example = "M5V2H1", description = "North American postal code used for Geocoder. Canada used in example.")
	private String postalCode;
	
	@OneToOne(mappedBy = "address")
	@JsonBackReference
	@Schema(hidden = true)
	private Customer customer;
}
