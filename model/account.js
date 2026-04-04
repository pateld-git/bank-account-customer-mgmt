import mongoose from 'mongoose';

const accountSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    balance: {
        type: Number,
        required: true,
        default: 0
    },
    createdAt: {
        type: Date,
        default: Date.now
    }
});

accountSchema.statics.findByIdSafe = async function (id) {
    const account = await this.findById(id);
    if (!account) {
        const error = new Error('Account not found with provided ID.');
        error.statusCode = 404;
        throw error;
    }
    return account;
};

const Account = mongoose.model('Account', accountSchema);
export default Account;