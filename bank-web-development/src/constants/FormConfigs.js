export const ADD_PAGE_CONFIGS = {
    customer: {
        title: "Add New Customer",
        fields: [
            {
                name: "type",
                label: "Customer Type",
                component: "select",
                options: ["Person", "Company"],
                required: true
            },
            { name: "name", label: "Name", required: true },
            { name: "streetNumber", label: "Street Number", required: true },
            { name: "postalCode", label: "Postal Code", required: true },
        ],
    },
    account: {
        title: "Open New Account",
        fields: [
            {
                name: "accountType",
                label: "Account Type",
                component: "select",
                options: ["Savings", "Checking"],
                required: true,
            },
            {
                name: "customerId",
                label: "Customer ID",
                type: "number",
                min: "1",
                required: true,
            },
            {
                name: "balance",
                label: "Initial Deposit",
                type: "number",
                step: "0.01",
                min: "0.01",
                required: true,
            },
        ],
    },
};

export const UPDATE_PAGE_CONFIGS = {
    customer: {
        title: "Update Customer Information",
        fields: [
            {
                name: "customerId",
                label: "Customer ID",
                type: "number",
                min: "1",
                required: true,
            },
            { name: "firstName", label: "First Name", required: true },
            { name: "lastName", label: "Last Name", required: true },
            { name: "street", label: "Street", required: true },
            { name: "zipCode", label: "Zip Code", required: true },
        ],
    },
    account: {
        title: "Update Account Balance",
        fields: [
            {
                name: "accountId",
                label: "Account ID",
                type: "number",
                min: "1",
                required: true,
            },
            {
                name: "balance",
                label: "New Balance",
                type: "number",
                step: "0.01",
                min: "0.01",
                required: true,
            },
        ],
    },
};