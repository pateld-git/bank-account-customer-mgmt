export const DUMMY_CUSTOMERS = [
    {
        id: 1,
        firstName: "Alice",
        lastName: "Johnson",
        email: "alice.j@example.com",
        city: "New York",
        zipCode: "10001",
        status: "Active"
    },
    {
        id: 2,
        firstName: "Bob",
        lastName: "Smith",
        email: "bob.smith@example.com",
        city: "Los Angeles",
        zipCode: "90001",
        status: "Inactive"
    },
    {
        id: 3,
        firstName: "Charlie",
        lastName: "Davis",
        email: "charlie.d@example.com",
        city: "Chicago",
        zipCode: "60601",
        status: "Pending"
    }
];

export const DUMMY_ACCOUNTS = [
    {
        id: 101,
        accountType: "Checking",
        balance: 2500.50, // Tests your Currency Atom
        customer: DUMMY_CUSTOMERS[0], // References Alice
        createdAt: "2024-01-15"
    },
    {
        id: 102,
        accountType: "Savings",
        balance: 12750.00,
        customer: DUMMY_CUSTOMERS[1], // References Bob
        createdAt: "2023-11-20"
    },
    {
        id: 103,
        accountType: "Credit",
        balance: -450.25, // Tests how your Currency Atom handles negatives
        customer: DUMMY_CUSTOMERS[0], // Alice again (One customer, multiple accounts)
        createdAt: "2024-02-10"
    }
];