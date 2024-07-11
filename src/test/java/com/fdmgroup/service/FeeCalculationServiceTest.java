package com.fdmgroup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FeeCalculationServiceTest {
	
	private FeeCalculationServiceImpl feeCalculationServiceImpl;
	
//	@Test
//	void test() {
//		// Arrange
//		
//		// Act
//
//		// Assert
//
//	}
	
	@BeforeEach
	void setUp() throws Exception {
		feeCalculationServiceImpl = new FeeCalculationServiceImpl();
	}
	
	@Test
	void testWhenBalance_LessThanOrEqual$100_Return$20Fee() {
		// Arrange
		double expectedResult = 20.00;
		
		// Stubbing
		
		// Act
		double actualResult = feeCalculationServiceImpl.calculateFee(0);
		// Assert

	}
	
}
