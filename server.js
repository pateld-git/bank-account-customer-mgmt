import dotenv from 'dotenv';
import express from 'express';
import connectDB from './database/db.js';
import accountRoutes from './routes/account-routes.js';
import transactionRoutes from './routes/transaction-routes.js';
import authRoutes from './routes/auth-routes.js';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

await connectDB();

app.use(express.json());

app.use('/auth', authRoutes);
app.use('/:userId/accounts', accountRoutes);
app.use('/:userId/accounts', transactionRoutes);

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});