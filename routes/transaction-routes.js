import express from 'express';
import authMiddleware from '../middleware/auth-middleware.js';
import { deposit, withdraw, transfer, viewTransactionHistoryPerAccount } from '../controller/transaction-controller.js';

const router = express.Router({ mergeParams: true });

router.post('/:accountId/deposit', authMiddleware, deposit);
router.post('/:accountId/withdraw', authMiddleware, withdraw);
router.post('/:accountId/transfer', authMiddleware, transfer);
router.get('/:accountId/transactions', authMiddleware, viewTransactionHistoryPerAccount);

export default router;