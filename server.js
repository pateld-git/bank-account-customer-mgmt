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

app.use('/accounts', accountRoutes);
app.use('/accounts', transactionRoutes);
app.use('/auth', authRoutes);

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});