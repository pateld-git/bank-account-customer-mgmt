import Account from "../model/account.js";
import Transaction from "../model/transaction.js";
import { checkInsufficientBalance } from '../service/transaction-service.js';
import { validateAmount } from '../service/account-service.js';

const deposit = async (req, res) => {
    try {
        const { accountId } = req.params;
        const { amount, description } = req.body;

        const accountDetails = await Account.findById(accountId);

        if (!accountDetails) {
            return res.status(404).json({
                success: false,
                message: 'Account not found with provided ID.'
            });
        }

        const validatedAmount = validateAmount(amount);

        accountDetails.balance += validatedAmount;
        await accountDetails.save();

        const newTransaction = await Transaction.create({
            accountId,
            type: 'DEPOSIT',
            amount: validatedAmount,
            description: description || `Deposit: $${validatedAmount}`
        });

        res.status(200).json({
            success: true,
            message: `Successfully deposited ${amount}`,
            data: newTransaction
        });

    } catch (error) {
        console.error("Error depositing money:", error);

        res.status(error.statusCode || 500).json({
            success: false,
            message: error.message || 'Something went wrong! Please try again.'
        });
    }
};

const withdraw = async (req, res) => {
    try {
        const { accountId } = req.params;
        const { amount, description } = req.body;

        const accountDetails = await Account.findById(accountId);

        if (!accountDetails) {
            return res.status(404).json({
                success: false,
                message: 'Account not found with provided ID.'
            });
        }

        const validatedAmount = validateAmount(amount);
        checkInsufficientBalance(accountDetails.balance, validatedAmount);

        accountDetails.balance -= validatedAmount;
        await accountDetails.save();

        const newTransaction = await Transaction.create({
            accountId: accountDetails._id,
            type: 'WITHDRAWAL',
            amount: validatedAmount,
            description: description || `Withdrawal: $${validatedAmount}`
        });

        res.status(200).json({
            success: true,
            message: `Successfully withdrew ${amount}`,
            data: newTransaction
        });
    } catch (error) {
        console.error("Error withdrawing money:", error);

        res.status(error.statusCode || 500).json({
            success: false,
            message: error.message || 'Something went wrong! Please try again.'
        });
    }
};

const transfer = async (req, res) => {
    try {
        const { accountId: sourceAccountId } = req.params;
        const { toAccountId, amount, description } = req.body;

        if (sourceAccountId === toAccountId) {
            return res.status(400).json({
                success: false,
                message: 'Cannot transfer money to the same account.'
            });
        }

        const validatedAmount = validateAmount(amount);

        const sourceAccount = await Account.findById(sourceAccountId);

        if (!sourceAccount) {
            return res.status(404).json({
                success: false,
                message: 'Source account not found.'
            });
        }

        checkInsufficientBalance(sourceAccount.balance, validatedAmount);

        const destinationAccount = await Account.findById(toAccountId);

        if (!destinationAccount) {
            return res.status(404).json({
                success: false,
                message: 'Destination account not found.'
            });
        }

        sourceAccount.balance -= validatedAmount;
        destinationAccount.balance += validatedAmount;
        await sourceAccount.save();
        await destinationAccount.save();

        const newTransaction = await Transaction.create({
            accountId: sourceAccount._id,
            toAccountId: destinationAccount._id,
            type: 'TRANSFER',
            amount: validatedAmount,
            description: description || `Transfer to account ${toAccountId} of $${validatedAmount}`
        });

        res.status(200).json({
            success: true,
            message: `Successfully transferred ${amount}.`,
            data: newTransaction
        });
    } catch (error) {
        console.error("Error transferring money:", error);

        res.status(error.statusCode || 500).json({
            success: false,
            message: error.message || 'Something went wrong! Please try again.'
        });
    }
};

const viewTransactionHistoryPerAccount = async (req, res) => {
    try {
        const { accountId } = req.params;
        const account = await Account.findById(accountId);

        if (!account) {
            return res.status(404).json({
                success: false,
                message: 'Account not found with provided ID.'
            });
        }

        const transactionHistory = await Transaction.find({
            $or: [
                { accountId: accountId },
                { toAccountId: accountId }
            ]
        }).sort({
            createdAt: -1
        });

        if (transactionHistory.length === 0) {
            return res.status(404).json({
                success: false,
                message: 'No transaction history found.'
            });
        }

        res.status(200).json({
            success: true,
            message: 'Transaction history fetched successfully',
            data: transactionHistory
        });

    } catch (error) {
        console.error("Error viewing transaction history:", error);

        res.status(error.statusCode || 500).json({
            success: false,
            message: error.message || 'Something went wrong! Please try again.'
        });
    }
};

export { deposit, withdraw, transfer, viewTransactionHistoryPerAccount };