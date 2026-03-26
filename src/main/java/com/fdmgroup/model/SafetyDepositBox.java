package com.fdmgroup.model;

public abstract class SafetyDepositBox {
	private boolean isAlloted;
	private double id;

	public boolean isAlloted() {
		return isAlloted;
	}

	public void setAlloted(boolean isAlloted) {
		this.isAlloted = isAlloted;
	}

	public double getId() {
		return id;
	}

	public void setId(double id) {
		this.id = id;
	}

}
