import React from "react";
import PropTypes from "prop-types";
import "./Button.css";

const Button = ({
  onClick,
  children,
  type = "button",
  className = "default-button",
}) => {
  return (
    <button onClick={onClick} className={className} type={type}>
      {children}
    </button>
  );
};

Button.propTypes = {
  children: PropTypes.node.isRequired, // Allows text, icons, or other elements
  onClick: PropTypes.func,
  className: PropTypes.string,
  type: PropTypes.string,
};

export default Button;
