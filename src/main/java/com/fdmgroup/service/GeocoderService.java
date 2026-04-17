package com.fdmgroup.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fdmgroup.exception.ArgNotFoundException;
import com.fdmgroup.exception.GeocoderException;
import com.fdmgroup.model.customer.Address;
import com.fdmgroup.model.customer.CustomerDTO;
import com.fdmgroup.model.customer.GeocoderResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class GeocoderService {
	private final WebClient geocoderWebClient;

	public CustomerDTO getPostalCodeProvinceFromGeocoder(CustomerDTO customerDTO) {
		String postalCode = customerDTO.getAddress().getPostalCode();
		log.info("Finding province and city from postal code: {}", postalCode);

		GeocoderResponse geoResponse = fetchGeocodePostalCodeProcince(customerDTO.getAddress().getPostalCode());

		return insertPostalCodeProcinceInCustomer(customerDTO, geoResponse);
	}

	private GeocoderResponse fetchGeocodePostalCodeProcince(String postalCode) {
		return geocoderWebClient.get()
				.uri(uri -> uri.path("/")
						.queryParam("locate", postalCode)
						.queryParam("json", "1")
						.build())
				.retrieve()
				.onStatus(HttpStatusCode::is5xxServerError, _ -> Mono.error(
						new GeocoderException("External API error for postal code: " + postalCode)))
				.onStatus(HttpStatusCode::is4xxClientError, _ -> Mono.error(
	                    new ArgNotFoundException("Invalid request or postal code not found: " + postalCode)))
				.bodyToMono(GeocoderResponse.class)
				.block();
	}

	private CustomerDTO insertPostalCodeProcinceInCustomer(CustomerDTO dto, GeocoderResponse geo) {
		if (geo == null || geo.getStandard() == null) {
			throw new ArgNotFoundException(
					"No valid geocode data found for " + dto.getAddress().getPostalCode());
		}

		GeocoderResponse.Standard details = geo.getStandard();

		Address updatedAddress = dto.getAddress().toBuilder()
				.city(details.getCity())
				.province(details.getProv())
				.build();

		return dto.toBuilder()
				.address(updatedAddress)
				.build();
	}
}
