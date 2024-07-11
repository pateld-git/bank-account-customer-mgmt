package com.fdmgroup.service;

public class FeeCalculationServiceImpl implements FeeCalculationService {
	
	private double accountBalanceFee;

	@Override
	public double calculateFee(double balance) {
		if(balance <= 100)
			accountBalanceFee = 20.00;
		else if(balance > 100 || balance <= 500)
			accountBalanceFee = 15.00;
		else if(balance > 500 || balance <= 1_000)
			accountBalanceFee = 10.00;
		else if(balance > 1_000 || balance <= 2_000)
			accountBalanceFee = 5.00;
		else if(balance > 2_000)
			accountBalanceFee = 0.00;
		
		return accountBalanceFee;
	}
	
}
