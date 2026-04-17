package com.fdmgroup.customer.exception;

public class PostalCodeNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5370452810593740773L;

	public PostalCodeNotFoundException(String message) {
		super(message);
	}
}
