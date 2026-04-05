import Account from "../model/account.js";
import Transaction from "../model/transaction.js";
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
        error.statusCode = 400; // Bad Request
        throw error;
    }
    return numericAmount;
};

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
        error.statusCode = 400; // Bad Request
        throw error;
    }
};

/**
 * Helper function to create and save a transaction record.
 * @param {object} params - The parameters for creating a transaction.
 * @param {string} params.accountId - The ID of the account involved.
 * @param {'DEPOSIT'|'WITHDRAWAL'|'TRANSFER'} params.type - The type of transaction.
 * @param {number} params.amount - The amount of the transaction.
 * @param {string} [params.description] - An optional custom description for the transaction.
 * @param {string} [params.toAccountId] - The ID of the destination account for transfers.
 * @returns {Promise<import("../model/transaction.js").TransactionDocument>} The created transaction document.
 */
const createTransactionRecord = async ({ accountId, type, amount, description, toAccountId = null }) => {
    let finalDescription = description;

    if (!finalDescription) {
        switch (type) {
            case 'DEPOSIT': finalDescription = `Deposit: $${amount}`; break;
            case 'WITHDRAWAL': finalDescription = `Withdrawal: $${amount}`; break;
            case 'TRANSFER': finalDescription = `Transfer to account ${toAccountId} of $${amount}`; break;
            default: finalDescription = 'Generic transaction'; // Fallback for unexpected types
        }
    }

    const transactionData = { accountId, type, amount, description: finalDescription };
    if (toAccountId) transactionData.toAccountId = toAccountId;
    return await Transaction.create(transactionData);
};

export { validateAmount, checkInsufficientBalance, createTransactionRecord };