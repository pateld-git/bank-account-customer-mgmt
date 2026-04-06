/**
 * Checks if an account has sufficient balance for a given amount.
 * Throws an error if the balance is insufficient.
 * @param {number} accountBalance - The current balance of the account.
 * @param {number} amount - The amount to check against the balance.
 * @param {string} [errorMessage='Insufficient balance.'] - Custom error message.
 * @throws {Error} If the account balance is insufficient.
 */
const checkInsufficientBalance = (accountBalance, amount, errorMessage = 'Insufficient balance.') => {
    if (accountBalance < amount) {
        const error = new Error(errorMessage);
        error.statusCode = 400;
        throw error;
    }
};

export { checkInsufficientBalance };