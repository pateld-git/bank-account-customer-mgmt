import Account from '../model/account.js';
import { validateAmount } from '../service/account-service.js';

const createAccount = async (req, res) => {
    try {
        const { userId } = req.params;
        const { type, balance } = req.body;

        const validatedBalance = validateAmount(balance, 0, 'Initial balance is required and cannot be negative.');

        const newAccount = await Account.create({
            userId,
            type,
            balance: validatedBalance
        });

        res.status(201).json({
            success: true,
            message: "Account added successfully",
            data: newAccount
        });

    } catch (error) {
        console.error("Error creating account:", error);
        res.status(error.statusCode || 500).json({
            success: false,
            message: error.message || "Error creating new account."
        });
    }
}

const getAllAccounts = async (req, res) => {
    try {
        const { userId } = req.params;

        const accounts = userId
            ? await Account.find({ userId })
            : await Account.find();

        if (accounts?.length > 0) {
            res.status(200).json({
                success: true,
                message: 'List of Accounts fetched successfully',
                data: accounts
            });
        } else {
            res.status(404).json({
                success: false,
                message: 'No Accounts found.'
            });
        }
    } catch (error) {
        console.error("Error fetching accounts:", error);

        res.status(500).json({
            success: false,
            message: "Something went wrong! Please try again."
        });
    }
};

const getAccountById = async (req, res) => {
    try {
        const { userId, accountId } = req.params;

        const accountDetails = await Account.findById(accountId);

        if (!accountDetails) {
            return res.status(404).json({
                success: false,
                message: 'Account not found.'
            });
        }

        res.status(200).json({
            success: true,
            userId,
            data: accountDetails
        });

    } catch (error) {
        console.error("Error fetching account details:", error);

        res.status(error.statusCode || 500).json({
            success: false,
            message: 'Something went wrong! Please try again.'
        });
    }
};

const updateAccount = async (req, res) => {
    try {
        const { type, balance } = req.body;
        const { accountId } = req.params;
        const updatedAccount = await Account.findByIdAndUpdate(accountId, {
            type,
            balance
        },
            { returnDocument: 'after' }
        );

        if (!updatedAccount) {
            return res.status(404).json({
                success: false,
                message: 'Account not found with provided ID.'
            });
        } else {
            res.status(200).json({
                success: true,
                message: 'Account updated successfully',
                data: updatedAccount
            });
        }

    } catch (error) {
        console.error("Error updating account:", error);
        res.status(error.statusCode || 500).json({
            success: false,
            message: error.message || 'Something went wrong! Please try again.'
        });
    }
};

const deleteAccount = async (req, res) => {
    try {
        const { accountId } = req.params;
        const deletedAccount = await Account.findByIdAndDelete(accountId);

        if (!deletedAccount) {
            return res.status(404).json({
                success: false,
                message: 'Account not found with provided ID.'
            });
        } else {
            res.status(200).json({
                success: true,
                data: deletedAccount
            });
        }
    } catch (error) {
        console.error("Error deleting account:", error);
        res.status(500).json({
            success: false,
            message: 'Something went wrong! Please try again.'
        })
    }
};

export { createAccount, getAllAccounts, getAccountById, updateAccount, deleteAccount };