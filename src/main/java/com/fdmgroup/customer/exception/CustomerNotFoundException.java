package com.fdmgroup.customer.exception;

public class CustomerNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3772669432789159260L;
	
	public CustomerNotFoundException(String message){
		super(message);
	}
	
	public CustomerNotFoundException(String message, long id){
		super(message + id);
	}

}
