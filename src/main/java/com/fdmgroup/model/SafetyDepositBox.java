package com.fdmgroup.model;

public abstract class SafetyDepositBox {
	/*---------------------------------------------------------------------------------------------------------------*/
	/* Class Attributes */
	/*---------------------------------------------------------------------------------------------------------------*/
	// deposit box is already alloted or is available
	private boolean isAlloted;
	
	// unique safety deposit box ID number
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
