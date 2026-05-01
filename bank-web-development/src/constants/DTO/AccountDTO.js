/**
 * Data Transfer Object for Account information.
 */
export class AccountDTO {
    /**
     * Creates an instance of AccountDTO.
     * @param {Object} formData - The raw form data.
     * @param {string|number} formData.customerId - The ID of the customer owning the account.
     * @param {string} [formData.type] - The type of account (e.g., SAVINGS, CHECKING).
     * @param {number} formData.balance - The initial balance.
     * @param {string|number} [formData.interestRate] - The interest rate for savings accounts.
     * @param {string|number} [formData.nextCheckNumber] - The next check number for checking accounts.
     */
    constructor(formData) {
        this.customerId = formData.customerId;
        this.type = formData.type || "known-by-db";
        this.balance = formData.balance;
        this.interestRate = formData.interestRate || "0.0";
        this.nextCheckNumber = formData.nextCheckNumber || "1";
    }

    /**
     * Converts the DTO instance to a plain object for API payloads.
     * @returns {Object} The account payload.
     */
    toPayload() {
        return {
            customerId: this.customerId,
            type: this.type,
            balance: this.balance,
            interestRate: this.interestRate,
            nextCheckNumber: this.nextCheckNumber,
        };
    }
}