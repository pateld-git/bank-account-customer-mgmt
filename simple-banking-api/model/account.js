import mongoose from 'mongoose';

const accountSchema = new mongoose.Schema({
    customerName: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true
    },
    balance: {
        type: Number,
        required: true,
        default: 0
    },
    createdAt: {
        type: Date.now
    }
});

export const Account = mongoose.model('Account', accountSchema);