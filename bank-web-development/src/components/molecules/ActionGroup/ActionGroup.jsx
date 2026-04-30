import React from "react";
import Button from "../../atoms/Button/Button";
import "./ActionGroup.css";

const ActionGroup = ({ actions }) => {
  return (
    <div className="table-action-group">
      {actions.map((action, index) => (
        <Button
          key={index}
          onClick={action.onClick}
          className={`action-button ${action.className || ""}`}
        >
          {action.label}
        </Button>
      ))}
    </div>
  );
};

export default ActionGroup;
