import ActionGroup from "../components/molecules/ActionGroup/ActionGroup";

export const getCustomerColumns = (onDelete, onUpdate) => [
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
                    // Use the passed-in handlers here
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