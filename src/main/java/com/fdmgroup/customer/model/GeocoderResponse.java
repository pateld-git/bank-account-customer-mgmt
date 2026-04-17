package com.fdmgroup.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Wrapper for the response received from the external Geocoding service")
public class GeocoderResponse {
	@Schema(description = "The standardized address components returned by the API")
    private Standard standard;

    @Data
    public static class Standard {
    	@Schema(example = "Toronto", description = "The city associated with the provided postal code")
        private String city;
        
        @Schema(example = "ON", description = "The provincial/state code (e.g., ON, BC, QC)")
        private String prov;
    }
}