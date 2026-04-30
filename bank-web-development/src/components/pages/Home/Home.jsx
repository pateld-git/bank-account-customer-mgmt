import React from "react";
import DashboardTemplate from "../../templates/DashboardTemplate";

/**
 * Home template component that serves as the landing page.
 * It includes the Header organism.
 */
const Home = () => {
  return (
    <div>
      <DashboardTemplate>
        <main className="home-container">
          <h2>WELCOME</h2>
        </main>
      </DashboardTemplate>
    </div>
  );
};

export default Home;
