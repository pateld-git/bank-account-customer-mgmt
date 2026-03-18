package model;

public enum BankBalanceTier {
	BOTTOM_BALANCE(100.00, 20.00), LOW_BALANCE(500.00, 15.00), MEDIUM_BALANCE(1000.00, 10.00),
	HIGH_BALANCE(2000.00, 5.00);

	private final double maxTierBalance;
	private final double fee;

	BankBalanceTier(double maxTierBalance, double fee) {
		this.maxTierBalance = maxTierBalance;
		this.fee = fee;
	}

	public double getMaxTierBalance() {
		return maxTierBalance;
	}

	public double getFee() {
		return fee;
	}

}
