import React from "react";
import NavGroup from "../../molecules/NavGroup/NavGroup";
import "./Header.css";

/**
 * Header organism component that displays the application title as a link.
 *
 * @param {Object} props - The component properties.
 * @param {string} props.url - The destination URL for the header link.
 */
const Header = ({ links = [], className = "" }) => {
  return (
    <div className="header-container">
      <header className={className}>
        <NavGroup items={links} className="horizontal-nav" />
      </header>
    </div>
  );
};

export default Header;
