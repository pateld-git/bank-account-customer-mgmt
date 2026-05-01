export class AccountDTO {
    constructor(formData) {
        this.customerId = formData.customerId;
        this.type = formData.type || "known-by-db";
        this.balance = formData.balance;
        this.interestRate = formData.interestRate || "0.0";
        this.nextCheckNumber = formData.nextCheckNumber || "1";
    }

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