const API_BASE_URL = "http://localhost:8080/api/customers";

export const createCustomer = async (customerData) => {
    try {
        const response = await fetch(API_BASE_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(customerData),
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || "Failed to create customer");
        }

        return await response.json();
    } catch (error) {
        console.error("Service Error (createCustomer):", error);
        throw error;
    }
}

export const fetchAllCustomers = async () => {
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) throw new Error("Could not connect to the banking service.");
        return await response.json();
    } catch (error) {
        console.error("Error in fetchAllCustomers:", error);
        throw error;
    }
};

export const fetchCustomerById = async (id) => {
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`);

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


export const updateCustomer = async (id, customerData) => {
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(customerData),
        });

        if (!response.ok) {
            throw new Error(`Failed to update customer: ${response.statusText}`);
        }

        return await response.json();
    } catch (error) {
        console.error("Service Error:", error);
        throw error;
    }
};

export const deleteCustomer = async (id) => {
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
        console.error(`Error in deleteCustomer for ID ${id}:`, error);
    }
};

