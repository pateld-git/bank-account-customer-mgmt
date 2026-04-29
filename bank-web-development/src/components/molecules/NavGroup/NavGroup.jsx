import React from "react";
import NavLink from "../../atoms/NavLink/NavLink";
import "./NavGroup.css";

const NavGroup = ({ items = [], className = "" }) => {
  return (
    <div className={`nav-group-container ${className}`}>
      {items.map((item, index) => (
        <NavLink key={index} to={item.to} label={item.label} />
      ))}
    </div>
  );
};

export default NavGroup;
