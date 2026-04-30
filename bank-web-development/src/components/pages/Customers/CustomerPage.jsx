import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import DashboardTemplate from "../../templates/DashboardTemplate";
import Table from "../../organisms/Table/Table";
import {
  fetchAllCustomers,
  deleteCustomer,
} from "../../../services/CustomerService";
import { getCustomerColumns } from "../../../utils/TableColumns";
import "./CustomerPage.css";

/**
 * UsersPage component that serves as the default page for viewing and managing users.
 * It utilizes the DashboardTemplate for consistent layout.
 */
const CustomersPage = () => {
  const [customers, setCustomers] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchAllCustomers()
      .then(setCustomers)
      .catch((err) => setError(err.message))
      .finally(() => setIsLoading(false));
  }, []);

  const onDeleteConfirm = async (id) => {
    if (!window.confirm("Are you sure you want to delete this customer?"))
      return;

    try {
      await deleteCustomer(id);

      setCustomers((prev) => prev.filter((cust) => cust.customerId !== id));

      alert("Customer deleted successfully.");
    } catch (err) {
      setError(err.message);
    }
  };

  const onUpdate = (id) => {
    console.log("Update triggered for ID:", id);
    navigate(`/update/customer/${id}`);
  };

  const onViewAccounts = (id) => {
    console.log("View accounts triggered for ID:", id);
    navigate(`/customers/${id}/accounts`);
  };

  const columns = getCustomerColumns(onViewAccounts, onUpdate, onDeleteConfirm);

  return (
    <DashboardTemplate>
      <main className="users-page-container">
        <header className="users-page-header">
          <h2>Customer Management</h2>
          <p>View and manage bank customers.</p>
        </header>

        <section className="users-content-area">
          {isLoading ? (
            <div className="loading">Syncing Banking Records...</div>
          ) : error ? (
            <div className="error">Error: {error}</div>
          ) : customers.length > 0 ? (
            <Table columns={columns} data={customers} />
          ) : (
            <div className="empty-state">
              No customers found in the database.
            </div>
          )}
        </section>
      </main>
    </DashboardTemplate>
  );
};

export default CustomersPage;
