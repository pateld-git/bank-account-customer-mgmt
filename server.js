import dotenv from 'dotenv';
import express from 'express';
import connectDB from './database/db.js';
import accountRoutes from './routes/account-routes.js';
import transactionRoutes from './routes/transaction-routes.js';

dotenv.config();

const app = express();
const port = process.env.PORT || 3000;

connectDB();

app.use(express.json());

app.use('/accounts', accountRoutes);
app.use('/transactions', transactionRoutes);

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});