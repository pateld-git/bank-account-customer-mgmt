import React from "react";
import DashboardTemplate from "../../templates/DashboardTemplate";
import Button from "../../atoms/Button/Button";
import DynamicForm from "../../organisms/Dynamic Form/DynamicForm";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { UPDATE_PAGE_CONFIGS } from "../../../constants/FormConfigs";
import "./Update.css";

/**
 * Add component that serves as the page for adding new bank accounts or customers.
 * It utilizes the DashboardTemplate for consistent layout.
 */
const Update = () => {
  const { type, id } = useParams();
  const [activeType, setActiveType] = useState(null);

  useEffect(() => {
    if (type && UPDATE_PAGE_CONFIGS[type]) {
      setActiveType(type);
    }
  }, [type]);

  const getInitialData = () => {
    if (!id) return {};

    // 1. If we have a valid activeType, use the specific field name
    // 2. If we DON'T have an activeType yet (but have an ID),
    //    we just return the ID with a generic key or wait for selection.

    let idFieldName = "id"; // Default fallback
    if (activeType === "customer") idFieldName = "customerId";
    if (activeType === "account") idFieldName = "accountId";

    return { [idFieldName]: id };
  };

  return (
    <DashboardTemplate>
      <main className="update-container">
        <h2>
          {activeType
            ? UPDATE_PAGE_CONFIGS[activeType].title
            : "Update Records"}
        </h2>

        {/* Show buttons if:
           1. There is no 'type' in the URL 
           OR 
           2. The 'type' in the URL isn't one we recognize (invalid type)
        */}
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
            <DynamicForm
              key={activeType}
              fields={UPDATE_PAGE_CONFIGS[activeType].fields}
              initialData={getInitialData()}
              onSubmit={(data) => console.log("Update Submitted:", data)}
            />
          ) : (
            <p>
              {id
                ? `Ready to update ID: ${id}. Please select the record type above.`
                : "Please select an action above or use a direct link."}
            </p>
          )}
        </div>
      </main>
    </DashboardTemplate>
  );
};

export default Update;
