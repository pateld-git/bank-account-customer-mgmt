import React from "react";
import SearchBar from "../../molecules/SearchBar/SearchBar";
import NavGroup from "../../molecules/NavGroup/NavGroup";
import { slide as Menu } from "react-burger-menu";
import "./SideMenu.css";

/**
 * SideMenu component that provides a slide-out navigation menu.
 * It includes a SearchBar and a NavGroup with navigation links.
 */
const SideMenu = ({ links }) => {
  return (
    <Menu className="side-menu">
      <div className="side-menu-header">
        <SearchBar />
      </div>
      <div className="side-menu-nav">
        <NavGroup items={links} className="vertical-nav" />
      </div>
    </Menu>
  );
};

export default SideMenu;
