import React, { useState, useEffect } from "react";
import Header from "../organisms/Header/Header";
import SideMenu from "../organisms/SideMenu/SideMenu";
import { ROUTES } from "../../constants/Routes";
import "./DashboardTemplate.css";

/**
 * DashboardTemplate component.
 * Provides a consistent layout with a Header, SideNavBar, and a main content area.
 */
const DashboardTemplate = ({ children }) => {
  const [isDesktop, setIsDesktop] = useState(window.innerWidth > 768);

  useEffect(() => {
    const handleResize = () => {
      setIsDesktop(window.innerWidth > 768);
    };
    window.addEventListener("resize", handleResize);

    return () => window.removeEventListener("resize", handleResize);
  }, []);

  const navLinks = Object.values(ROUTES)
    .filter((route) => route.label)
    .map((route) => ({
      to: route.path,
      label: route.label,
    }));

  const homeLink = [
    { to: ROUTES.HOME.path, label: "Bank Account Application" },
  ];

  return (
    <div className="dashboard-layout">
      <SideMenu links={navLinks} />
      <Header links={homeLink} className="main-header" />
      {isDesktop && <Header links={navLinks} className="sub-header" />}

      <div className="dashboard-body">
        <div className="main-content-area">{children}</div>
      </div>
    </div>
  );
};

export default DashboardTemplate;
