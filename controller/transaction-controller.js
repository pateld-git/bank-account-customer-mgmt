import Account from "../model/account.js";
import Transaction from "../model/transaction.js";
import { validateAmount, checkInsufficientBalance } from '../service/transaction-service.js';

const deposit = async (req, res) => {
    try {
        const { id: accountId } = req.params;
        const { amount, description } = req.body;

        const accountDetails = await Account.findByIdSafe(accountId);

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
        const { id: accountId } = req.params;
        const { amount, description } = req.body;

        const accountDetails = await Account.findByIdSafe(accountId);

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
        const { id: sourceAccountId } = req.params;
        const { toAccountId, amount, description } = req.body;

        if (sourceAccountId === toAccountId) {
            return res.status(400).json({
                success: false,
                message: 'Cannot transfer money to the same account.'
            });
        }

        const validatedAmount = validateAmount(amount);

        const sourceAccount = await Account.findByIdSafe(sourceAccountId);
        checkInsufficientBalance(sourceAccount.balance, validatedAmount);

        const destinationAccount = await Account.findByIdSafe(toAccountId);

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

const viewTransactionHistory = async (req, res) => {
    try {
        const { id: accountId } = req.params;
        await Account.findByIdSafe(accountId);

        const transactionHistory = await Transaction.find({
            $or: [
                { accountId: accountId },
                { toAccountId: accountId }
            ]
        }).sort({
            createdAt: -1
        });

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

export { deposit, withdraw, transfer, viewTransactionHistory };