import Account from "../model/account.js";
import Transaction from "../model/transaction.js";

const deposit = async (req, res) => {
    try {
        const currentAccountId = req.params.id;
        const depositFormData = req.body;
        const accountDetails = await Account.findByIdSafe(currentAccountId);

        if (Number(depositFormData.amount) < 0.01) {
            return res.status(400).json({
                success: false,
                message: 'Cannot deposit less than $0.01'
            });
        }

        accountDetails.balance += Number(depositFormData.amount);
        await accountDetails.save();

        const newTransaction = await Transaction.create({
            accountId: currentAccountId,
            type: 'DEPOSIT',
            amount: depositFormData.amount,
            description: depositFormData.description || `Deposit: ${depositFormData.amount}`
        });

        res.status(200).json({
            success: true,
            message: `Successfully deposited ${depositFormData.amount}`,
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
        const getCurrentAccountID = req.params.id;
        const withdrawFormData = req.body;
        const accountDetails = await Account.findByIdSafe(getCurrentAccountID);

        if (accountDetails.balance < Number(withdrawFormData.amount)) {
            return res.status(400).json({
                success: false,
                message: 'Insufficient balance for this withdrawal.'
            });
        }

        accountDetails.balance -= Number(withdrawFormData.amount);
        await accountDetails.save();

        const newTransaction = await Transaction.create({
            accountId: accountDetails._id,
            type: 'WITHDRAWAL',
            amount: withdrawFormData.amount,
            description: withdrawFormData.description || `Withdrawal: ${withdrawFormData.amount}`
        });

        res.status(200).json({
            success: true,
            message: `Successfully withdrew ${withdrawFormData.amount}`,
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
        const sourceAccountId = req.params.id;
        const transferFormData = req.body;

        if (sourceAccountId === transferFormData.toAccountId) {
            return res.status(400).json({
                success: false,
                message: 'Cannot transfer money to the same account.'
            });
        }

        const sourceAccount = await Account.findByIdSafe(sourceAccountId);
        const destinationAccount = await Account.findByIdSafe(transferFormData.toAccountId);

        if (sourceAccount.balance < Number(transferFormData.amount)) {
            return res.status(400).json({
                success: false,
                message: 'Insufficient balance in the source account.'
            });
        } else if (transferFormData.amount < 0.01) {
            return res.status(400).json({
                success: false,
                message: 'Cannot deposit less than $0.01 to the destination account.'
            });
        }

        sourceAccount.balance -= Number(transferFormData.amount);
        destinationAccount.balance += Number(transferFormData.amount);

        await sourceAccount.save();
        await destinationAccount.save();

        const newTransaction = await Transaction.create({
            accountId: sourceAccount._id,
            toAccountId: destinationAccount._id,
            type: 'TRANSFER',
            amount: transferFormData.amount,
            description: transferFormData.description || `Transfer to account ${toAccountId}`
        });

        res.status(200).json({
            success: true,
            message: `Successfully transferred ${transferFormData.amount} to account ${transferFormData.toAccountId}`,
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
        const getCurrentAccountID = req.params.id;
        await Account.findByIdSafe(getCurrentAccountID);

        const transactionHistory = await Transaction.find({
            $or: [
                { accountId: getCurrentAccountID },
                { toAccountId: getCurrentAccountID }
            ]
        }).sort({
            createdAt: -1
        });

        res.status(200).json({
            success: true,
            message: 'Transaction history fetched successfully',
            Data: transactionHistory
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