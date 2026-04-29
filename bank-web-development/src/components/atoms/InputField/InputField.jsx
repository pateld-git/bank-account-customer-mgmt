import React from "react";
import "./InputField.css";

const InputField = ({
  id,
  name,
  type,
  value,
  onChange,
  placeholder,
  required,
}) => {
  return (
    <input
      type={type}
      name={name}
      value={value}
      placeholder={placeholder}
      onChange={onChange}
      id={id}
      className="custom-input"
    />
  );
};

export default InputField;
