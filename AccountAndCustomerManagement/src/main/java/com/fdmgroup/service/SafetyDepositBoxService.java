package com.fdmgroup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fdmgroup.model.SafetyDepositBox;
import com.fdmgroup.model.SmallSafetyDepositBox;

public class SafetyDepositBoxService {
	private static SafetyDepositBoxService safetyDepositBoxService;
	private List<SafetyDepositBox> safetyDepositBoxes;
	private static int numberOfSafetyDepositBox;
	private static boolean isFlagWait;

	private SafetyDepositBoxService() {
		safetyDepositBoxes = new ArrayList<>();
		setFlagWait(false);
	}

	public static synchronized SafetyDepositBoxService getInstance() {
		if (safetyDepositBoxService == null) {
			safetyDepositBoxService = new SafetyDepositBoxService();
		}

		return safetyDepositBoxService;
	}

	public static void setNumberOfSafetyDepositBox(int boxes) {
		numberOfSafetyDepositBox = boxes;
	}

	public static int getNumberOfSafetyDepositBox() {
		return numberOfSafetyDepositBox;
	}

	public synchronized SafetyDepositBox allocateSafetyDepositBox() {
		while (true) {
			Optional<SafetyDepositBox> availableBox = getReleasedSafetyDepositBox();
			if (availableBox.isPresent()) {
				availableBox.get().setAlloted(true);
				return availableBox.get();
			} else if (safetyDepositBoxes.size() < numberOfSafetyDepositBox) {
				SafetyDepositBox newBox = new SmallSafetyDepositBox();
				newBox.setAlloted(true);
				safetyDepositBoxes.add(newBox);
				return newBox;
			} else if (safetyDepositBoxes.size() == numberOfSafetyDepositBox) {
				try {
					setFlagWait(true);
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void releaseSafetyDepositBox(SafetyDepositBox box) {
		box.setAlloted(false);
		notifyAll();
	}

	public synchronized int getNumberOfAvailableSafetyDepositBox() {
		int count = safetyDepositBoxes.size();

		for (SafetyDepositBox box : safetyDepositBoxes) {
			if (box.isAlloted()) {
				count--;
			}
		}

		return count;
	}

	public synchronized Optional<SafetyDepositBox> getReleasedSafetyDepositBox() {
		for (SafetyDepositBox box : safetyDepositBoxes) {
			if (!box.isAlloted()) {
				return Optional.of(box);
			}
		}
		return Optional.empty();
	}

	public List<SafetyDepositBox> getSafetyDepositBoxes() {
		return safetyDepositBoxes;
	}

	public boolean isFlagWait() {
		return isFlagWait;
	}

	public static void setFlagWait(boolean wait) {
		isFlagWait = wait;
	}

}
