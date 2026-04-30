import React from "react";
import "./InputField.css";

const InputField = ({ className, ...props }) => {
  return (
    <input
      className={`custom-input ${className || "custom-input"}`}
      {...props}
    />
  );
};

export default InputField;
