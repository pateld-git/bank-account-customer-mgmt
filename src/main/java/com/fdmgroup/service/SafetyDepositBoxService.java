package com.fdmgroup.service;

import java.util.List;
import java.util.Optional;

import com.fdmgroup.model.SafetyDepositBox;

public class SafetyDepositBoxService {
	/*---------------------------------------------------------------------------------------------------------------*/
	/* Attributes */
	/*---------------------------------------------------------------------------------------------------------------*/
	private static SafetyDepositBoxService safetyDepositBoxService;
	
	private List<SafetyDepositBox> safetyDepositBoxes;
	
	private static int numberOfSafetyDepositBox;
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/* Singleton Method */
	/*---------------------------------------------------------------------------------------------------------------*/
	public static SafetyDepositBoxService getInstance() {
		if(safetyDepositBoxService == null)
			safetyDepositBoxService = new SafetyDepositBoxService();
		
		return safetyDepositBoxService;
	}
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/* Getter and Setter Methods */
	/*---------------------------------------------------------------------------------------------------------------*/
	public static int getNumberOfSafetyDepositBox() {
		return numberOfSafetyDepositBox;
	}

	public static void setNumberOfSafetyDepositBox(int numberOfSafetyDepositBox) {
		SafetyDepositBoxService.numberOfSafetyDepositBox = numberOfSafetyDepositBox;
	}
	
	public List<SafetyDepositBox> getSafetyDepositBoxes() {
		return safetyDepositBoxes;
	}
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/* Other Class Methods */
	/*---------------------------------------------------------------------------------------------------------------*/
	public SafetyDepositBox allocateSafetyDepositBox() {
		
		return null;
	}
	
	public void releaseSafetyDepositBox(SafetyDepositBox releaseSafetyDepositBox) {
		
	}
	
	public int getNumberOfAvailableSafetyDepositBoxes() {
		return 0;
	}
	
	public Optional<SafetyDepositBox> getReleasedSafetyDepositBox(){
		return null;
	}

	
	
}
