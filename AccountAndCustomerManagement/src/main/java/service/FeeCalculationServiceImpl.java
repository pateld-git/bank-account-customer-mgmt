package service;

import model.BankBalanceTier;

public class FeeCalculationServiceImpl implements FeeCalculationService {

	public double calculateFee(double balance) {
		for (BankBalanceTier tier : BankBalanceTier.values()) {
			if (balance <= tier.getMaxTierBalance()) {
				return tier.getFee();
			}
		}

		return 0;
	}

}
