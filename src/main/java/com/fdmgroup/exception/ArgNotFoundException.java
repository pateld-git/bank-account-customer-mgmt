package com.fdmgroup.exception;

public class ArgNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3521325313982912055L;
	
	public ArgNotFoundException(String message) {
		super(message);
	}
}
