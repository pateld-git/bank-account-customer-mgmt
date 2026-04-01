import express from 'express';
import { createAccount, getAllAccounts, getAccountById, updateAccount, deleteAccount } from '../controller/account-controller.js';

const router = express.Router();

router.post('/create', createAccount);
router.get('/get', getAllAccounts);
router.get('/get/:id', getAccountById);
router.put('/update/:id', updateAccount);
router.delete('/delete/:id', deleteAccount);

export default router;