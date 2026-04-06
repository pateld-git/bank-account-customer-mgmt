import { MIN_TRANSACTION_AMOUNT } from "../util/constants.js";

/**
 * Validates if an amount is a valid number and meets a minimum threshold.
 * Throws an error if validation fails.
 * @param {any} amount - The amount to validate.
 * @param {number} [minAmount=MIN_TRANSACTION_AMOUNT] - The minimum allowed amount.
 * @param {string} [errorMessage='Invalid amount provided.'] - Custom error message.
 * @returns {number} The validated numeric amount.
 * @throws {Error} If the amount is invalid or below the minimum.
 */
const validateAmount = (amount, minAmount = MIN_TRANSACTION_AMOUNT, errorMessage = 'Invalid amount provided.') => {
    const numericAmount = Number(amount);
    if (isNaN(numericAmount) || numericAmount < minAmount) {
        const error = new Error(errorMessage);
        error.statusCode = 400;
        throw error;
    }
    return numericAmount;
};

export { validateAmount };
