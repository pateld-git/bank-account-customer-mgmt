package com.fdmgroup.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.fdmgroup.model.SafetyDepositBox;

class TestSafetyDepositBoxService {
	private SafetyDepositBoxService safetyDepositBoxService;
	private int threadSleepTime;

	@BeforeEach
	void setUp() {
		safetyDepositBoxService = SafetyDepositBoxService.getInstance();
		safetyDepositBoxService.getSafetyDepositBoxes().clear();
		SafetyDepositBoxService.setFlagWait(false);

		threadSleepTime = 5000; // in milliseconds
	}

	@Test
	void test_SafetyDepositBoxService_IsASingleton() {
		SafetyDepositBoxService boxService1 = SafetyDepositBoxService.getInstance();
		SafetyDepositBoxService boxService2 = SafetyDepositBoxService.getInstance();

		assertSame(boxService1, boxService2, "\nDifferent Instances: " + boxService1 + "\n" + boxService2 + "\n");
	}

	@Test
	void test_getReleasedSafetyDepositBox_ReturnsEmpty_WhenAllBoxesAreAlloted() {
		Optional<SafetyDepositBox> availableBox = safetyDepositBoxService.getReleasedSafetyDepositBox();

		assertTrue(availableBox.isEmpty(), "There should be no available Safety Deposit Boxes.");
	}

	@Test
	void test_AllocateSafetyDepositBox_CreatesNewSafetyDepositBoxWhenPoolIsEmpty() {
		SafetyDepositBoxService.setNumberOfSafetyDepositBox(2);

		SafetyDepositBox box1 = safetyDepositBoxService.allocateSafetyDepositBox();
		SafetyDepositBox box2 = safetyDepositBoxService.allocateSafetyDepositBox();

		assertNotNull(box1, "Box 1 is null.");
		assertNotNull(box2, "Box 2 is null.");
		assertNotSame(box1, box2, "Reuse of boxes.");
//		assertEquals(2, safetyDepositBoxService.getSafetyDepositBoxes().size());
	}

	@Test
	void test_AllocateSafetyDepositBox_ReturnsAvailableBoxFromPool() {
		SafetyDepositBoxService.setNumberOfSafetyDepositBox(2);

		SafetyDepositBox oldBox = safetyDepositBoxService.allocateSafetyDepositBox();
		safetyDepositBoxService.releaseSafetyDepositBox(oldBox);

		SafetyDepositBox reUseBox = safetyDepositBoxService.allocateSafetyDepositBox();

		assertNotNull(oldBox, "Old Box is null.");
		assertNotNull(reUseBox, "Reusing box is null");
		assertSame(oldBox, reUseBox, "Box Not Reused");
		assertTrue(reUseBox.isAlloted());
	}

	@ParameterizedTest(name = "Allocating {0} and Releasing {1} Should Result in {2} Available Boxes")
	@CsvSource({ "0, 0, 0", "1, 0, 0", "1, 1, 1", "2, 0, 0", "2, 1, 1", "2, 2, 2" })
	void test_getNumberOfAvailableSafetyDepositBoxes_ReturnsTheCountOfAvailableBoxes(int allocateBoxes,
			int releaseBoxes, int expectedAvailableBoxes) {

		SafetyDepositBoxService.setNumberOfSafetyDepositBox(allocateBoxes);

		for (int i = 0; i < allocateBoxes; i++) {
			safetyDepositBoxService.allocateSafetyDepositBox();
		}

		for (int i = 0; i < releaseBoxes; i++) {
			SafetyDepositBox box = safetyDepositBoxService.getSafetyDepositBoxes().get(i);
			safetyDepositBoxService.releaseSafetyDepositBox(box);
		}

		// Act
		int actualAvailableBoxes = safetyDepositBoxService.getNumberOfAvailableSafetyDepositBox();

		// Assert
		assertEquals(expectedAvailableBoxes, actualAvailableBoxes);
	}

	@Test
	@DisplayName("Two Threads Request Boxes, No Waiting, Hold Box for 5 sec, Release Boxes")
	void test_createTwoThreads_EachThreadRequestsABox_NoThreaShouldBeKeptWaiting_AndAllThreadsBoxesReleasedAfter5Seconds() {
		Thread thread0 = new Thread(new Runnable() {
			@Override
			public void run() {
				SafetyDepositBox box = safetyDepositBoxService.allocateSafetyDepositBox();
				try {
					Thread.sleep(threadSleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				safetyDepositBoxService.releaseSafetyDepositBox(box);
			}
		});

		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				SafetyDepositBox box = safetyDepositBoxService.allocateSafetyDepositBox();
				try {
					Thread.sleep(threadSleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				safetyDepositBoxService.releaseSafetyDepositBox(box);
			}
		});

		thread0.start();
		thread1.start();

		try {
			thread0.join();
			thread1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(2, safetyDepositBoxService.getSafetyDepositBoxes().size());
		assertFalse(safetyDepositBoxService.getSafetyDepositBoxes().get(0).isAlloted());
		assertFalse(safetyDepositBoxService.getSafetyDepositBoxes().get(1).isAlloted());
		assertFalse(safetyDepositBoxService.isFlagWait());
	}

	@Test
	@DisplayName("Three Threads Request Boxes, One Waiting, Hold Box for 5 sec, Release Boxes, Wait Picks Up Box")
	void test_createThreeThreads_EachThreadRequestsABox_OneThreaShouldBeKeptWaiting() {
		Thread thread0 = new Thread(() -> {
			SafetyDepositBox box = safetyDepositBoxService.allocateSafetyDepositBox();
			try {
				Thread.sleep(threadSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			safetyDepositBoxService.releaseSafetyDepositBox(box);
		});

		Thread thread1 = new Thread(() -> {
			SafetyDepositBox box = safetyDepositBoxService.allocateSafetyDepositBox();
			try {
				Thread.sleep(threadSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			safetyDepositBoxService.releaseSafetyDepositBox(box);
		});

		Thread thread2 = new Thread(() -> {
			SafetyDepositBox box = safetyDepositBoxService.allocateSafetyDepositBox();
			try {
				Thread.sleep(threadSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			safetyDepositBoxService.releaseSafetyDepositBox(box);
		});

		thread0.start();
		thread1.start();
		thread2.start();

		try {
			thread0.join();
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(2, safetyDepositBoxService.getSafetyDepositBoxes().size());
		assertFalse(safetyDepositBoxService.getSafetyDepositBoxes().get(0).isAlloted());
		assertFalse(safetyDepositBoxService.getSafetyDepositBoxes().get(1).isAlloted());
		assertTrue(safetyDepositBoxService.isFlagWait());
	}
}
