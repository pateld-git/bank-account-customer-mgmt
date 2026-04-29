import React from "react";
import { NavLink as RouterNavLink } from "react-router-dom";
import "./NavLink.css";

const NavLink = ({ to, label, className = "" }) => {
  return (
    <RouterNavLink to={to} className="nav-link">
      {label}
    </RouterNavLink>
  );
};

export default NavLink;
