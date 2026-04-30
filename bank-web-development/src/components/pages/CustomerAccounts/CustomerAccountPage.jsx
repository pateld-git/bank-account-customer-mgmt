import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import DashboardTemplate from "../../templates/DashboardTemplate";
import Table from "../../organisms/Table/Table";
import {
  fetchAccountsByCustomerId,
  deleteAccount,
} from "../../../services/AccountService";
import { getAccountColumns } from "../../../utils/TableColumns";
import "./CustomerAccountPage.css";
// import "../Customers/CustomerPage.css";

/**
 * CustomerAccountsPage component that serves as the default page for viewing and managing accounts for a specified user.
 * It utilizes the DashboardTemplate for consistent layout.
 */
const AccountPage = () => {
  const { id } = useParams();
  const [accounts, setAccounts] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const loadAccounts = async () => {
      try {
        setIsLoading(true);
        const data = await fetchAccountsByCustomerId(id);
        setAccounts(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setIsLoading(false);
      }
    };

    if (id) {
      loadAccounts();
    }
  }, [id]);

  const onUpdate = (id) => {
    console.log("Update triggered for ID:", id);
    navigate(`/update/account/${id}`);
  };

  const onDeleteConfirm = async (id) => {
    if (!window.confirm("Are you sure you want to delete this account?"))
      return;

    try {
      await deleteAccount(id);

      setAccounts((prev) => prev.filter((cust) => cust.accountId !== id));

      alert("Account deleted successfully.");
    } catch (err) {
      setError(err.message);
    }
  };

  const columns = getAccountColumns(onUpdate, onDeleteConfirm);

  return (
    <DashboardTemplate>
      <main className="accounts-page-container">
        <header className="accounts-page-header">
          <h2>Account Management</h2>
          <p>View and manage bank accounts for customer {id}.</p>
        </header>

        <section className="accounts-content-area">
          {isLoading ? (
            <div className="loading">Syncing Banking Records...</div>
          ) : error ? (
            <div className="error">Error: {error}</div>
          ) : accounts.length > 0 ? (
            <Table columns={columns} data={accounts} />
          ) : (
            <div className="empty-state">
              No accounts found in the database.
            </div>
          )}
        </section>
      </main>
    </DashboardTemplate>
  );
};

export default AccountPage;
