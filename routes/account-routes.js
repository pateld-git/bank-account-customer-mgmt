import express from 'express';
import { createAccount, getAllAccounts, getAccountById, updateAccount, deleteAccount } from '../controller/account-controller.js';
import authMiddleware from '../middleware/auth-middleware.js';

const router = express.Router({ mergeParams: true });

router.post('/', authMiddleware, createAccount);
router.get('/', authMiddleware, getAllAccounts);
router.get('/:accountId', authMiddleware, getAccountById);
router.put('/:accountId', authMiddleware, updateAccount);
router.delete('/:accountId', authMiddleware, deleteAccount);

export default router;