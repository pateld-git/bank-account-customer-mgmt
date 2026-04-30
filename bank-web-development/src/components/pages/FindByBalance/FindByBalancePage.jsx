import React, { useState } from "react";
import DashboardTemplate from "../../templates/DashboardTemplate";
import SearchBar from "../../molecules/SearchBar/SearchBar";
import Table from "../../organisms/Table/Table";
import Currency from "../../atoms/Currency/Currency";
import ActionGroup from "../../molecules/ActionGroup/ActionGroup";
import { DUMMY_ACCOUNTS } from "../../../1-dummy-data/dummy-data";
import "./FindByBalancePage.css";

const FindByBalancePage = () => {
  const [filteredData, setFilteredData] = useState(DUMMY_ACCOUNTS);

  const handleSearch = (query) => {
    if (!query) return setFilteredData(DUMMY_ACCOUNTS);
    const results = DUMMY_ACCOUNTS.filter(
      (acc) => acc.balance >= parseFloat(query),
    );
    setFilteredData(results);
  };

  const columns = [
    { key: "id", label: "Account #" },
    { key: "type", label: "Account Type" },
    {
      key: "balance",
      label: "Current Balance",
      render: (row) => <Currency value={row.balance} />,
    },
    {
      key: "actions",
      label: "Actions",
      render: (row) => (
        <ActionGroup
          actions={[
            {
              label: "Update",
              onClick: () => console.log("View", row.id),
              className: "btn-update",
            },
            {
              label: "Delete",
              onClick: () => console.log("Edit", row.id),
              className: "btn-delete",
            },
            {
              label: "Customer Details",
              onClick: () => console.log("Edit", row.id),
              className: "btn-view",
            },
          ]}
        />
      ),
    },
  ];

  return (
    <DashboardTemplate>
      <div className="page-content-wrapper">
        <div className="balance-search-container">
          <header className="page-header">
            <h2 className="page-title">Find Records by Minimum Balance</h2>
            <p className="page-subtitle">
              Search and manage account records based on balance thresholds.
            </p>
          </header>

          <section className="search-bar-section">
            <SearchBar
              placeholder="Min balance (e.g. 500)..."
              inputType="number"
              onSearch={handleSearch}
            />
          </section>

          <section className="results-container">
            {filteredData.length > 0 ? (
              <Table columns={columns} data={filteredData} />
            ) : (
              <div className="empty-results">No records match your search.</div>
            )}
          </section>
        </div>
      </div>
    </DashboardTemplate>
  );
};

export default FindByBalancePage;
