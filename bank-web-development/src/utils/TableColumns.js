import ActionGroup from "../components/molecules/ActionGroup/ActionGroup";

export const getCustomerColumns = (onViewAccounts, onUpdate, onDelete) => [
    { key: "customerId", label: "ID" },
    { key: "name", label: "Full Name" },
    { key: "type", label: "Entity Type" },
    {
        key: "street",
        label: "Street",
        render: (row) => row.address?.streetNumber || "N/A"
    },
    {
        key: "city",
        label: "City",
        render: (row) => row.address?.city || "N/A"
    },
    {
        key: "province",
        label: "State",
        render: (row) => row.address?.province || "N/A"
    },
    {
        key: "actions",
        label: "Actions",
        render: (row) => (
            <ActionGroup
                actions={[
                    {
                        label: "Accounts",
                        className: "btn-view",
                        onClick: () => onViewAccounts(row.customerId)
                    },
                    {
                        label: "UPDATE",
                        className: "btn-update",
                        onClick: () => onUpdate(row.customerId)
                    },
                    {
                        label: "DELETE",
                        className: "btn-delete",
                        onClick: () => onDelete(row.customerId)
                    },
                ]}
            />
        ),
    },
];

export const getAccountColumns = (onDelete, onUpdate) => [
    { key: "accountId", label: "ID" },
    { key: "type", label: "Entity Type" },
    { key: "balance", label: "Balance" },
    { key: "interestRate", label: "Interest Rate" },
    { key: "nextCheckNumber", label: "Next Check Number" },
    {
        key: "actions",
        label: "Actions",
        render: (row) => (
            <ActionGroup
                actions={[
                    {
                        label: "UPDATE",
                        className: "btn-update",
                        onClick: () => onUpdate(row.accountId)
                    },
                    {
                        label: "DELETE",
                        className: "btn-delete",
                        onClick: () => onDelete(row.accountId)
                    },
                ]}
            />
        ),
    },
];