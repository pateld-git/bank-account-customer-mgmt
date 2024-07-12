package com.fdmgroup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fdmgroup.model.Account;
import com.fdmgroup.model.CheckingAccount;

public class FeeCalculationServiceTest {
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Attributes*/
	/*---------------------------------------------------------------------------------------------------------------*/
	double testBalance;
	private Account testAccount;
	private FeeCalculationService feeCalculationServiceTest;
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Set Up Methods*/ 
	/*---------------------------------------------------------------------------------------------------------------*/
	@BeforeEach
	void setUp() throws Exception {
		testAccount = new CheckingAccount();
		feeCalculationServiceTest= new FeeCalculationServiceImpl();
	}
	
	/*---------------------------------------------------------------------------------------------------------------*/
	/*Test Methods*/ 
	/*---------------------------------------------------------------------------------------------------------------*/
	@Test
	void testWhenBalance_lessThanOrEqual$100_return$20Fee() {
		// Arrange
		testBalance = 100d;
		testAccount.correctBalance(testBalance);
		double expectedResult = 20d;
		
		// Act
		double actualResult = feeCalculationServiceTest.calculateFee(testAccount.getBalance());
		
		// Assert
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void testWhenBalance_greaterThan$100_orLessThanOrEqual$500_return$15Fee() {
		// Arrange
		testBalance = 500d;
		testAccount.correctBalance(testBalance);
		double expectedResult = 15d;
		
		// Act
		double actualResult = feeCalculationServiceTest.calculateFee(testAccount.getBalance());
		
		// Assert
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void testWhenBalance_greaterThan$500_orLessThanOrEqual$1k_return$10Fee() {
		// Arrange
		testBalance = 1_000d;
		testAccount.correctBalance(testBalance);
		double expectedResult = 10d;
		
		// Act
		double actualResult = feeCalculationServiceTest.calculateFee(testAccount.getBalance());
		
		// Assert
		assertEquals(expectedResult, actualResult);
		//assertEquals(testAccount.getBalance(), testBalance);
	}
	
	@Test
	void testWhenBalance_greaterThan$1k_orLessThanOrEqual$2k_return$5Fee() {
		// Arrange
		testBalance = 2_000d;
		testAccount.correctBalance(testBalance);
		
		double expectedResult = 5d;
		
		// Act
		double actualResult = feeCalculationServiceTest.calculateFee(testAccount.getBalance());
		
		// Assert
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void testWhenBalance_greaterThan$2k_return$0Fee() {
		// Arrange
		testBalance = 2_001d;
		testAccount.correctBalance(testBalance);
		
		double expectedResult = 0d;
		
		// Act
		double actualResult = feeCalculationServiceTest.calculateFee(testAccount.getBalance());
		
		// Assert
		assertEquals(expectedResult, actualResult);
	}
	
}
