import React, { useState } from "react";
import DashboardTemplate from "../../templates/DashboardTemplate";
import SearchBar from "../../molecules/SearchBar/SearchBar";
import InputField from "../../atoms/InputField/InputField";
import Table from "../../organisms/Table/Table";
import {
  fetchCustomerById,
  deleteCustomer,
} from "../../../services/CustomerService";
import {
  fetchAccountById,
  deleteAccount,
} from "../../../services/AccountService";
import {
  getCustomerColumns,
  getAccountColumns,
} from "../../../utils/TableColumns";
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
   * @param {number|string} id - The ID of the record to update.
   */
  const handleUpdate = (id) => {
    const type = searchType.customer ? "customer" : "account";
    navigate(`/update/${type}/${id}`);
  };

  /**
   * Handles record deletion.
   * @param {number|string} id - The ID of the record to delete.
   */
  const handleDelete = async (id) => {
    const typeLabel = searchType.customer ? "customer" : "account";
    if (!window.confirm(`Are you sure you want to delete this ${typeLabel}?`))
      return;

    try {
      if (searchType.customer) {
        await deleteCustomer(id);
      } else {
        await deleteAccount(id);
      }
      alert("Record deleted successfully.");
      setResult(null);
    } catch (err) {
      setError("Failed to delete the record. Please try again later.");
    }
  };

  /**
   * Navigates to the accounts view for a specific customer.
   * @param {number|string} id - The customer ID.
   */
  const onViewAccounts = (id) => {
    console.log("View accounts triggered for ID:", id);
    navigate(`/customers/${id}/accounts`);
  };

  /**
   * Handles the logic for switching between Customer and Account search types.
   * @param {React.ChangeEvent<HTMLInputElement>} e - The change event from the checkbox.
   */
  const handleCheckboxChange = (e) => {
    const { name, checked } = e.target;
    if (!checked) return;

    setSearchType({
      customer: name === "customer",
      account: name === "account",
    });
  };

  /**
   * Fetches data from the service based on the selected search type and ID.
   * @param {string} value - The ID value entered in the search bar.
   */
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
        data = await fetchAccountById(value);
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

  const customerColumns = getCustomerColumns(
    onViewAccounts,
    handleUpdate,
    handleDelete,
  );
  const accountColumns = getAccountColumns(handleDelete, handleUpdate);
  const activeColumns = searchType.customer ? customerColumns : accountColumns;

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
