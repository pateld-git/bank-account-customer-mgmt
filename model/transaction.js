import mongoose from "mongoose";

const transactionSchema = new mongoose.Schema({
    accountId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Account',
        required: true,
        index: true
    },
    type: {
        type: String,
        enum: ['DEPOSIT', 'WITHDRAWAL', 'TRANSFER'],
        required: true,
        index: true
    },
    toAccountId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Account',
        required: function () { return this.type === 'TRANSFER'; },
        index: true
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

// Compound indexes for fast history retrieval (both directions) sorted by date
transactionSchema.index({ accountId: 1, createdAt: -1 });
transactionSchema.index({ toAccountId: 1, createdAt: -1 });

const Transaction = mongoose.model('Transaction', transactionSchema);
export default Transaction;
