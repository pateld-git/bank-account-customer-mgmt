import React from "react";
import DashboardTemplate from "../../templates/DashboardTemplate";
import Button from "../../atoms/Button/Button";
import DynamicForm from "../../organisms/Dynamic Form/DynamicForm";
import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { UPDATE_PAGE_CONFIGS } from "../../../constants/FormConfigs";
import { updateCustomer } from "../../../services/CustomerService";
import { updateAccount } from "../../../services/AccountService";
import { CustomerDTO } from "../../../constants/DTO/CustomerDTO";
import { AccountDTO } from "../../../constants/DTO/AccountDTO";
import "./Update.css";

/**
 * Add component that serves as the page for adding new bank accounts or customers.
 * It utilizes the DashboardTemplate for consistent layout.
 */
const Update = () => {
  const { type, id } = useParams();
  const navigate = useNavigate();
  const [activeType, setActiveType] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (type && UPDATE_PAGE_CONFIGS[type]) {
      setActiveType(type);
    }
  }, [type]);

  const getInitialData = () => {
    if (!id) return {};

    if (activeType === "customer") {
      return { customerId: id };
    } else if (activeType === "account") {
      return { accountId: id };
    }

    return {};
  };

  const handleFormSubmit = async (formData) => {
    const targetId =
      activeType === "customer" ? formData.customerId : formData.accountId;

    let payload;
    if (activeType === "customer") {
      payload = new CustomerDTO(formData).toPayload();
    } else {
      payload = new AccountDTO(formData).toPayload();
    }

    console.log(payload);

    const isConfirmed = window.confirm(
      `Are you sure you want to update this ${activeType}?`,
    );
    if (!isConfirmed) return;

    setIsLoading(true);
    setError(null);

    try {
      if (activeType === "customer") {
        await updateCustomer(targetId, payload);
        alert("Customer updated successfully!");
      } else if (activeType === "account") {
        await updateAccount(targetId, payload);
        alert("Account update logged to console.");
      }
      navigate("/customers");
    } catch (err) {
      setError(err.message || "An error occurred while updating.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <DashboardTemplate>
      <main className="update-container">
        <h2>
          {activeType
            ? UPDATE_PAGE_CONFIGS[activeType].title
            : "Update Records"}
        </h2>

        {(!type || !UPDATE_PAGE_CONFIGS[type]) && (
          <div className="button-group">
            <Button onClick={() => setActiveType("customer")}>
              Update Customer
            </Button>
            <Button onClick={() => setActiveType("account")}>
              Update Account
            </Button>
          </div>
        )}

        <div className="form-display-area">
          {activeType ? (
            <div className={isLoading ? "form-loading" : ""}>
              <DynamicForm
                key={activeType}
                fields={UPDATE_PAGE_CONFIGS[activeType].fields}
                initialData={getInitialData()}
                onSubmit={handleFormSubmit}
              />
              {isLoading && <p>Updating record...</p>}
            </div>
          ) : (
            <p>
              {id
                ? `Ready to update ID: ${id}. Please select the record type above.`
                : "Please select an action above or use a direct link."}
            </p>
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

export default Update;
