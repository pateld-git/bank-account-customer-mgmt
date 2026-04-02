import mongoose from "mongoose";

const transactionSchema = new mongoose.Schema({
    accountId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Account',
        required: true
    },
    type: {
        type: String,
        enum: ['DEPOSIT', 'WITHDRAWAL', 'TRANSFER'],
        required: true
    },
    toAccountId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Account',
        required: function () { return this.type === 'TRANSFER'; }
    },
    createdAt: {
        type: Date,
        default: Date.now
    },
    amount: {
        type: Number,
        required: true
    },
    description: {
        type: String,
        required: true
    }
});

const Transaction = mongoose.model('Transaction', transactionSchema);
export default Transaction;
