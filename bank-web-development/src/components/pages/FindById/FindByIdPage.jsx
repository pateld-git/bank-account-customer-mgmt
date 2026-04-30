import React, { useState } from "react";
import DashboardTemplate from "../../templates/DashboardTemplate";
import SearchBar from "../../molecules/SearchBar/SearchBar";
import InputField from "../../atoms/InputField/InputField";
import Table from "../../organisms/Table/Table";
import {
  fetchCustomerById,
  deleteCustomer,
} from "../../../services/CustomerService";
// import { fetchAccountById } from "../../../services/AccountService";
import { getCustomerColumns } from "../../../utils/TableColumns";
import "./FindByIdPage.css";
import { useNavigate } from "react-router-dom";

/**
 * FindByIdPage component that allows users to search for specific records by ID.
 * It utilizes the DashboardTemplate for consistent layout.
 */
const FindByIdPage = () => {
  const navigate = useNavigate();
  const [searchType, setSearchType] = useState({
    customer: true,
    account: false,
  });

  const [result, setResult] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  /**
   * Navigates to the Update page with the correct type and ID.
   */
  const handleUpdate = (id) => {
    const type = searchType.customer ? "customer" : "account";
    navigate(`/update/${type}/${id}`);
  };

  /**
   * Handles record deletion.
   */
  const handleDelete = async (id) => {
    if (
      !window.confirm(
        `Are you sure you want to delete this ${searchType.customer ? "customer" : "account"}?`,
      )
    ) {
      return;
    }

    try {
      if (searchType.customer) {
        await deleteCustomer(id);
      } else {
        // await deleteAccount(id);
      }
      alert("Record deleted successfully.");
      setResult(null); // Clear the table after deletion
    } catch (err) {
      setError("Failed to delete the record. Please try again later.");
    }
  };

  const handleCheckboxChange = (e) => {
    const { name, checked } = e.target;
    if (!checked) return;

    setSearchType({
      customer: name === "customer",
      account: name === "account",
    });
  };

  const handleSearch = async (value) => {
    if (!value) return;

    setIsLoading(true);
    setError(null);
    setResult(null);

    try {
      let data;
      if (searchType.customer) {
        data = await fetchCustomerById(value);
      } else if (searchType.account) {
        console.log("Account search triggered for ID:", value);
        throw new Error("404");
      }
      setResult(data);
    } catch (err) {
      if (err.status === 404) {
        setError(err.message);
      } else {
        setError(
          "There was an error communicating with the server. Please try again later.",
        );
      }
    } finally {
      setIsLoading(false);
    }
  };

  const customerColumns = getCustomerColumns(handleDelete, handleUpdate);
  const activeColumns = searchType.customer ? customerColumns : [];

  return (
    <DashboardTemplate>
      <main className="find-by-id-page">
        <header className="page-header">
          <h2>Find Record by ID</h2>
          <p>Select the record type and enter an ID.</p>
        </header>

        <section className="search-section">
          <div className="search-controls">
            <SearchBar
              placeholder={`${searchType.customer ? "Customer" : "Account"} ID...`}
              onSearch={handleSearch}
              inputType="number"
              min="1"
              required="true"
            />

            <div className="filter-group">
              <label className="checkbox-label">
                <InputField
                  type="checkbox"
                  name="customer"
                  checked={searchType.customer}
                  onChange={handleCheckboxChange}
                  className="checkbox-input"
                />
                Customer
              </label>
              <label className="checkbox-label">
                <InputField
                  type="checkbox"
                  name="account"
                  checked={searchType.account}
                  onChange={handleCheckboxChange}
                  className="checkbox-input"
                />
                Account
              </label>
            </div>
          </div>
        </section>

        <section className="results-container">
          {isLoading && (
            <div className="status-message loading">
              Syncing with Banking Server...
            </div>
          )}

          {error && (
            <div className="status-message error-box">
              <p>{error}</p>
            </div>
          )}

          {!isLoading && !error && result && (
            <div className="search-result-content">
              <h3 className="result-title">Record Found</h3>
              <Table columns={activeColumns} data={[result]} />
            </div>
          )}

          {!isLoading && !error && !result && (
            <div className="status-message empty-state">
              <p>No record selected. Please use the search bar above.</p>
            </div>
          )}
        </section>
      </main>
    </DashboardTemplate>
  );
};

export default FindByIdPage;
