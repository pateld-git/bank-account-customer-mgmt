import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/customers";

/**
 * Creates a new customer.
 */
export const createCustomer = async (customerData) => {
    try {
        const response = await axios.post(API_BASE_URL, customerData);
        return response.data;
    } catch (error) {
        const message = error.response?.data?.message || "Failed to create customer";
        console.error("Service Error (createCustomer):", message);
        throw new Error(message);
    }
};

/**
 * Fetches all customers.
 */
export const fetchAllCustomers = async () => {
    try {
        const response = await axios.get(API_BASE_URL);
        return response.data;
    } catch (error) {
        console.error("Error in fetchAllCustomers:", error);
        throw new Error("Could not connect to the banking service.");
    }
};

/**
 * Fetches a single customer by ID.
 */
export const fetchCustomerById = async (id) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/${id}`);
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
 * Updates an existing customer.
 */
export const updateCustomer = async (id, customerData) => {
    try {
        const response = await axios.put(`${API_BASE_URL}/${id}`, customerData);
        return response.data;
    } catch (error) {
        const message = error.response?.data?.message || "Failed to update customer";
        console.error("Service Error (updateCustomer):", message);
        throw new Error(message);
    }
};

/**
 * Deletes a customer by ID.
 */
export const deleteCustomer = async (id) => {
    try {
        await axios.delete(`${API_BASE_URL}/${id}`);
        return true;
    } catch (error) {
        const errorData = error.response?.data || {};
        const message = errorData.message || `Failed to delete: ${error.response?.status}`;

        console.error(`Error in deleteCustomer for ID ${id}:`, message);
        throw new Error(message);
    }
};