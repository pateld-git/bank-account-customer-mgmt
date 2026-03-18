package service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.BankBalanceTier;

public class FeeCalculationServiceImpl implements FeeCalculationService {
	private static final Logger LOGGER = LogManager.getLogger(FeeCalculationServiceImpl.class);

	public double calculateFee(double balance) {
		for (BankBalanceTier tier : BankBalanceTier.values()) {
			if (balance <= tier.getMaxTierBalance()) {
				LOGGER.debug("Balance matches Tier: {}", tier);
				return tier.getFee();
			}
		}

		LOGGER.warn("No matching Tier for balance ${} defined in BankBalanceTier. Fee set to $0", balance);
		return 0;
	}

}
