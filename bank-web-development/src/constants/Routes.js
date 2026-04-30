import Home from "../components/pages/Home/Home";
import AddPage from "../components/pages/Add/Add";
import UpdatePage from "../components/pages/Update/Update";
import FindByIdPage from "../components/pages/FindById/FindByIdPage";
import FindByBalancePage from "../components/pages/FindByBalance/FindByBalancePage";
import CustomersPage from "../components/pages/Customers/CustomerPage";


/**
 * Configuration object for application routing.
 * Defines the path, navigation label, and component element for each route.
 * 
 * @typedef {Object} RouteConfig
 * @property {string} path - The URL path for the route.
 * @property {string} label - The display name for navigation links.
 * @property {JSX.Element} element - The React component to render for this route.
 */

/**
 * The collection of all available routes in the application.
 * @type {Record<string, RouteConfig>}
 */

export const ROUTES = {
    HOME: { path: "/", label: "Home", element: <Home /> },
    ADD: { path: "/add", label: "Add", element: <AddPage /> },
    UPDATE: { path: "/updated", label: "Update", element: <UpdatePage /> },
    CUSTOMERS: { path: "/customers", label: "Customers", element: <CustomersPage /> },
    FIND_BY_ID: { path: "/find-by-id", label: "Find by ID", element: <FindByIdPage /> },
    FIND_BY_BALANCE: { path: "/find-by-balance", label: "Find by Balance", element: <FindByBalancePage /> },
};