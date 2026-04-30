import { Routes, Route } from 'react-router-dom';
import { ROUTES } from "./constants/Routes";
import './App.css';
import Update from './components/pages/Update/Update';
import CustomerAccountPage from './components/pages/CustomerAccounts/CustomerAccountPage';
import PageNotFound from './components/pages/PageNotFound/PageNotFound';

function App() {
  return (
    <div className="App">
      <Routes>
        {Object.values(ROUTES).map((route) => (
          <Route key={route.path} path={route.path} element={route.element} />
        ))}
        <Route path="/home" element={ROUTES.HOME.element} />
        <Route path="/update/:type/:id" element={<Update />} />
        <Route path="/customers/:id/accounts" element={<CustomerAccountPage />} />

        <Route path="*" element={<PageNotFound />} />
      </Routes>
    </div >
  );
}

export default App;
