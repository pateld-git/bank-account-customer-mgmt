package tests.service;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import service.FeeCalculationService;
import service.FeeCalculationServiceImpl;

class TestFeeCalculationService {
	private static final Logger LOGGER = LogManager.getLogger(TestFeeCalculationService.class);
	private FeeCalculationService feeService = new FeeCalculationServiceImpl();

	@ParameterizedTest(name = "Balance of {0} should result in a fee of {1}")
	@CsvSource({ "-1.01, 20.00", "0.00, 20.00", "99.99, 20.00", "100.00, 20.00", "100.01, 15.00", "500.00, 15.00",
			"500.01, 10.00", "1000.00, 10.00", "1000.01, 5.00", "2000.00, 5.00", "2000.01, 0.00" })
	void test_CalculateFeeReturnsCorrectFeeValues_BasedUponBalanceRanges(double balance, double expectedFee) {
		LOGGER.info("test_test_CalculateFeeReturnsCorrectFeeValues_BasedUponBalanceRanges");
		
		double actualFee = feeService.calculateFee(balance);

		assertEquals(expectedFee, actualFee, 0.01);
		try {
			assertEquals(expectedFee, actualFee, 0.01);
			LOGGER.debug("Assertions Successful: Expected {} matches Actual {}", expectedFee, actualFee);
		} catch (AssertionError e) {
			LOGGER.error("Test failed Expected: {}, Actual: {}", expectedFee, actualFee);
			throw e;
		}
	}

}
