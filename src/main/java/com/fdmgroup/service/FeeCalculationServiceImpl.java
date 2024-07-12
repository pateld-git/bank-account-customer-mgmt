package com.fdmgroup.service;

public class FeeCalculationServiceImpl implements FeeCalculationService {
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Attributes*/
	/*---------------------------------------------------------------------------------------------------------------*/
	private double accountBalanceFee;

	/*---------------------------------------------------------------------------------------------------------------*/
	/*Implemented Methods*/
	/*---------------------------------------------------------------------------------------------------------------*/
	@Override
	public double calculateFee(double balance) {
		if(balance > 2_000)
			accountBalanceFee = 0d;
		
		if(balance > 1_000 && balance <= 2_000)
			accountBalanceFee = 5d;
		
		else if(balance > 500 && balance <= 1_000)
			accountBalanceFee = 10d;
		
		else if(balance > 100 && balance <= 500)
			accountBalanceFee = 15d;
		
		else if(balance <= 100)
			accountBalanceFee = 20d;
		
		return accountBalanceFee;
	}
	
}
