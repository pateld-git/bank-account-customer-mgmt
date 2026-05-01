import React, { useState, useEffect } from "react";
import Button from "../../atoms/Button/Button";
import InputField from "../../atoms/InputField/InputField";
import "./DynamicForm.css";

const DynamicForm = ({
  title,
  fields,
  placeholder,
  onSubmit,
  initialData = {},
}) => {
  const [formData, setFormData] = useState(() => {
    const initialState = {};
    fields.forEach((field) => {
      initialState[field.name] = initialData[field.name] || "";
    });
    return initialState;
  });

  useEffect(() => {
    const initialState = {};
    fields.forEach((field) => {
      initialState[field.name] = initialData[field.name] || "";
    });
    setFormData(initialState);
  }, [fields, initialData]);

  const handleSubmit = (e) => {
    e.preventDefault();

    onSubmit(formData);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const renderField = (field) => {
    const commonProps = {
      id: field.name,
      name: field.name,
      value: formData[field.name] || "",
      onChange: handleChange,
      required: field.required,
      placeholder: field.placeholder || field.label,
      min: field.min,
      step: field.step,
      max: field.max,
    };

    switch (field.component) {
      case "select":
        return (
          <select {...commonProps}>
            <option value="">Select {field.label}</option>
            {field.options?.map((opt) => (
              <option key={opt} value={opt}>
                {opt}
              </option>
            ))}
          </select>
        );

      case "input":
      default:
        return <InputField {...commonProps} type={field.type || "text"} />;
    }
  };

  return (
    <form className="dynamic-form" onSubmit={handleSubmit}>
      {fields.map((field) => (
        <div className="form-group" key={field.name}>
          <label htmlFor={field.name}>{field.label}</label>
          {renderField(field)}
        </div>
      ))}
      <Button type="submit">Submit</Button>
    </form>
  );
};

export default DynamicForm;
