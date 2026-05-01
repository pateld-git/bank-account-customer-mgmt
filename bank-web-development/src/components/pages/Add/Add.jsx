import React from "react";
import DashboardTemplate from "../../templates/DashboardTemplate";
import Button from "../../atoms/Button/Button";
import DynamicForm from "../../organisms/Dynamic Form/DynamicForm";
import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { ADD_PAGE_CONFIGS } from "../../../constants/FormConfigs";
import { createCustomer } from "../../../services/CustomerService";
import { CustomerDTO } from "../../../constants/DTO/CustomerDTO";
import { AccountDTO } from "../../../constants/DTO/AccountDTO";
import "./Add.css";
import { createAccount } from "../../../services/AccountService";

/**
 * Add component that serves as the page for adding new bank accounts or customers.
 * It utilizes the DashboardTemplate for consistent layout.
 */
const Add = () => {
  const navigate = useNavigate();
  const { customerId } = useParams();
  const [activeType, setActiveType] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (customerId) {
      setActiveType("account");
    }
  }, [customerId]);

  const handleFormSubmit = async (formData) => {
    let payload;

    if (activeType === "customer") {
      payload = new CustomerDTO(formData).toPayload();
    } else {
      payload = new AccountDTO(formData).toPayload();
    }
    console.log(payload);

    const isConfirmed = window.confirm(
      `Are you sure you want to add this new ${activeType}?`,
    );

    if (!isConfirmed) return;

    setIsLoading(true);
    setError(null);

    try {
      if (activeType === "customer") {
        await createCustomer(payload);
        alert("Customer created successfully!");
        navigate("/customers");
      } else if (activeType === "account") {
        await createAccount(payload);
        alert("Account opened successfully!");
        navigate(`/customers/${formData.customerId}/accounts`);
      }
    } catch (err) {
      setError(err.message || "An error occurred while saving.");
    } finally {
      setIsLoading(false);
    }
  };

  console.log(activeType);
  return (
    <DashboardTemplate>
      <main className="add-container">
        <h2>
          {activeType ? ADD_PAGE_CONFIGS[activeType].title : "Add Records"}
        </h2>

        {!customerId && (
          <div className="button-group">
            <Button onClick={() => setActiveType("customer")}>
              Add Customer
            </Button>
            <Button onClick={() => setActiveType("account")}>
              Add Account
            </Button>
          </div>
        )}

        <div className="form-display-area">
          {activeType ? (
            <div className={isLoading ? "form-loading" : ""}>
              <DynamicForm
                key={activeType}
                fields={ADD_PAGE_CONFIGS[activeType].fields}
                initialData={activeType === "account" ? { customerId } : {}}
                onSubmit={handleFormSubmit}
              />
              {isLoading && <p>Processing Request...</p>}
            </div>
          ) : (
            <p>Please select an action above.</p>
          )}
        </div>

        {error && (
          <div className="error-message-box">
            <p className="error-message-text">
              <strong>Error:</strong> {error}
            </p>
          </div>
        )}
      </main>
    </DashboardTemplate>
  );
};

export default Add;
