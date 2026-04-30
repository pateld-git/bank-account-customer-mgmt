import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import DashboardTemplate from "../../templates/DashboardTemplate";
import Table from "../../organisms/Table/Table";
import {
  fetchAllCustomers,
  deleteCustomer,
} from "../../../services/CustomerService";
import { getCustomerColumns } from "../../../utils/TableColumns";
import "./CustomerAccountPage.css";

/**
 * UsersPage component that serves as the default page for viewing and managing users.
 * It utilizes the DashboardTemplate for consistent layout.
 */
const AccountPage = () => {
  const [customers, setCustomers] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  return <DashboardTemplate></DashboardTemplate>;
};

export default AccountPage;
