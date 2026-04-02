import Account from "../model/account.js";
import Transaction from "../model/transaction.js";

const deposit = async (req, res) => {
    try {
        const { id: accountId } = req.params;
        const { amount, description } = req.body;

        const accountDetails = await Account.findByIdSafe(accountId);

        if (Number(amount) < 0.01) {
            return res.status(400).json({
                success: false,
                message: 'Cannot deposit less than $0.01'
            });
        }

        accountDetails.balance += Number(amount);
        await accountDetails.save();

        const newTransaction = await Transaction.create({
            accountId,
            type: 'DEPOSIT',
            amount,
            description: description || `Deposit: ${amount}`
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

        if (Number(amount) < 0.01) {
            return res.status(400).json({
                success: false,
                message: 'Minimum withdrawal amount is $0.01'
            });
        }

        if (accountDetails.balance < Number(amount)) {
            return res.status(400).json({
                success: false,
                message: 'Insufficient balance for this withdrawal.'
            });
        }

        accountDetails.balance -= Number(amount);
        await accountDetails.save();

        const newTransaction = await Transaction.create({
            accountId: accountDetails._id,
            type: 'WITHDRAWAL',
            amount,
            description: description || `Withdrawal: ${amount}`
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

        const sourceAccount = await Account.findByIdSafe(sourceAccountId);
        const destinationAccount = await Account.findByIdSafe(toAccountId);

        if (sourceAccount.balance < Number(amount)) {
            return res.status(400).json({
                success: false,
                message: 'Insufficient balance in the source account.'
            });
        } else if (Number(amount) < 0.01) {
            return res.status(400).json({
                success: false,
                message: 'Minimum transfer amount is $0.01'
            });
        }

        sourceAccount.balance -= Number(amount);
        destinationAccount.balance += Number(amount);

        await sourceAccount.save();
        await destinationAccount.save();

        const newTransaction = await Transaction.create({
            accountId: sourceAccount._id,
            toAccountId: destinationAccount._id,
            type: 'TRANSFER',
            amount,
            description: description || `Transfer to account ${toAccountId}`
        });

        res.status(200).json({
            success: true,
            message: `Successfully transferred ${amount} to account ${toAccountId}`,
            data: newTransaction
        });
    } catch (error) {
        console.log("Error transferring money:", error);

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
        console.log("Error viewing transaction history:", error);

        res.status(error.statusCode || 500).json({
            success: false,
            message: error.message || 'Something went wrong! Please try again.'
        });
    }
};

export { deposit, withdraw, transfer, viewTransactionHistory };