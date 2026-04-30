const API_BASE_URL = "http://localhost:8080/api/customers";

export const createCustomer = async (customerData) => {
    const response = await fetch(API_BASE_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(customerData),
    });
    if (!response.ok) throw new Error("Failed to create customer");
    return response.json();
};

export const fetchAllCustomers = async () => {
    const response = await fetch(API_BASE_URL);
    if (!response.ok) throw new Error("Could not connect to the banking service.");
    return await response.json();
};

export const fetchCustomerById = async (id) => {
    const response = await fetch(`${API_BASE_URL}/${id}`);

    if (!response.ok) {
        const errorData = await response.json();

        const error = new Error(errorData.message || "An unexpected error occurred");

        error.status = response.status;
        error.timestamp = errorData.timestamp;

        throw error;
    }

    return await response.json();
};

export const deleteCustomer = async (id) => {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
    });

    if (!response.ok) {
        throw new Error("Failed to delete the record from the database.");
    }

    return true;
};

