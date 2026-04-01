import Account from '../model/account.js';

const createAccount = async (req, res) => {
    try {
        const newAccountFormData = req.body;
        const newAccount = await Account.create(newAccountFormData);

        if (newAccount) {
            res.status(201).json({
                success: true,
                message: "Account added successfully",
                data: newAccount
            });
        }
    } catch (error) {
        console.error("Error adding new book:", error);

        res.status(500).json({
            success: false,
            message: "Error adding new book."
        })
    }
}

const getAllAccounts = async (_req, res) => {
    try {
        const allAccounts = await Account.find();

        if (allAccounts?.length > 0) {
            res.status(200).json({
                success: true,
                message: 'List of Accounts fetched successfully',
                data: allAccounts
            });
        } else {
            res.status(404).json({
                success: false,
                message: 'No Accounts found'
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
        const getCurrentAccountID = req.params.id;
        const accountDetails = await Account.findById(getCurrentAccountID);

        if (!accountDetails) {
            return res.status(404).json({
                success: false,
                message: 'Account not found with provided ID.'
            });
        } else {
            res.status(200).json({
                success: true,
                data: accountDetails
            });
        }
    } catch (error) {
        console.error("Error fetching account details:", error);

        res.status(500).json({
            success: false,
            message: 'Something went wrong! Please try again.'
        });
    }
};

const updateAccount = async (req, res) => {
    try {
        const updatedAccountFormData = req.body;
        const currentAccountID = req.params.id;
        const updatedAccount = await Account.findByIdAndUpdate(currentAccountID, updatedAccountFormData, {
            returnDocument: 'after'
        });

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
            })
        }

    } catch (error) {
        console.error("Error updating account:", error);
        res.status(500).json({
            success: false,
            message: 'Something went wrong! Please try again.'
        })
    }
};

const deleteAccount = async (req, res) => {
    try {
        const getCurrentBoodId = req.params.id;
        const deletedAccount = await Account.findByIdAndDelete(getCurrentBoodId);

        if (!deletedAccount) {
            return res.status(404).json({
                success: false,
                message: 'Account not found with provided ID.'
            });
        } else {
            res.status(200).json({
                success: true,
                data: deletedAccount
            })
        }
    } catch (error) {
        console.error("Error deleting account:", error);
        res.status.json({
            success: false,
            message: 'Something went wrong! Please try again.'
        })
    }
};

export { createAccount, getAllAccounts, getAccountById, updateAccount, deleteAccount };