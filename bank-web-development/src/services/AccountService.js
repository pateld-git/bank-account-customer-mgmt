const API_BASE_URL = "http://localhost:8080/api/accounts";

export const fetchAccountsByCustomerId = async (id) => {
    try {
        const response = await fetch(`${API_BASE_URL}/search-customer?customerId=${id}`);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            const error = new Error(errorData.message || "Customer not found");
            error.status = response.status;
            error.timestamp = errorData.timestamp;
            throw error;
        }
        return await response.json();
    } catch (error) {
        console.error(`Service Error (fetchById - ID: ${id}):`, error);
        throw error;
    }
};

export const deleteAccount = async (id) => {
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || `Failed to delete: ${response.status}`);
        }

        return true;
    } catch (error) {
        console.error(`Error in deleteAccount for ID ${id}:`, error);
    }
};
