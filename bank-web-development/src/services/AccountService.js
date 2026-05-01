import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/accounts";

/**
 * Sends account data to the backend API.
 * @param {Object} accountData - The account data object.
 * @param {string|number} accountData.customerId - The ID of the customer owning the account.
 * @returns {Promise<Object>} The created account data.
 * @throws {Error} If the creation fails.
 */
export const createAccount = async (accountData) => {
    const { customerId, ...payload } = accountData;
    try {
        const response = await axios.post(`${API_BASE_URL}/customer/${customerId}`, payload);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || "Failed to create account");
    }
};

/**
 * Fetches accounts for a specific customer.
 * @param {string|number} id - The customer ID.
 * @returns {Promise<Array>} A list of accounts associated with the customer.
 * @throws {Error} If the customer is not found or the request fails.
 */
export const fetchAccountsByCustomerId = async (id) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/search-customer`, {
            params: { customerId: id }
        });

        return response.data;
    } catch (error) {
        const errorData = error.response?.data || {};
        const customError = new Error(errorData.message || "Customer not found");

        customError.status = error.response?.status;
        customError.timestamp = errorData.timestamp;

        console.error(`Service Error (fetchById - ID: ${id}):`, customError);
        throw customError;
    }
};

/**
 * Fetches a single account by its ID.
 * @param {number|string} id - The unique identifier of the account.
 * @returns {Promise<Object>} The account data.
 * @throws {Error} If the account is not found or the request fails.
 */

export const fetchAccountById = async (id) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/${id}`);
        return response.data;
    } catch (error) {
        const errorData = error.response?.data || {};
        const customError = new Error(errorData.message || "Account not found");
        customError.status = error.response?.status;
        throw customError;
    }
};

/**
 * Updates an account by ID.
 * @param {string|number} id - The unique identifier of the account.
 * @param {Object} accountData - The updated account data.
 * @returns {Promise<Object>} The updated account data.
 * @throws {Error} If the update fails.
 */
export const updateAccount = async (id, accountData) => {
    try {
        const response = await axios.put(`${API_BASE_URL}/${id}`, accountData);
        return response.data;
    } catch (error) {
        console.error("Update Service Error:", error.response?.data || error.message);
        throw new Error(error.response?.data?.message || "Failed to update account");
    }
};

/**
 * Deletes an account by ID.
 * @param {string|number} id - The unique identifier of the account.
 * @returns {Promise<boolean>} True if deletion was successful.
 * @throws {Error} If the deletion fails.
 */
export const deleteAccount = async (id) => {
    try {
        await axios.delete(`${API_BASE_URL}/${id}`);
        return true;
    } catch (error) {
        throw new Error(error.response?.data?.message || "Failed to delete account");
    }
};