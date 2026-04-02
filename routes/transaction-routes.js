import express from 'express';
import { deposit, withdraw, transfer, viewTransactionHistory } from '../controller/transaction-controller.js';

const router = express.Router();

router.post('/:id/deposit', deposit);
router.post('/:id/withdraw', withdraw);
router.post('/:id/transfer', transfer);
router.get('/:id/transactions', viewTransactionHistory);

export default router;