import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/accounts";

/**
 * Sends account data to the backend API.
 * @param {Object} accountData - The payload from the DTO.
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
 * Axios handles query params cleanly via the 'params' object.
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
 * Updates an account by ID.
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
 */
export const deleteAccount = async (id) => {
    try {
        await axios.delete(`${API_BASE_URL}/${id}`);
        return true;
    } catch (error) {
        const errorData = error.response?.data || {};
        const message = errorData.message || `Failed to delete: ${error.response?.status}`;

        console.error(`Error in deleteAccount for ID ${id}:`, message);
        throw new Error(message);
    }
};