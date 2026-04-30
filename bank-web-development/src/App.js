import { Routes, Route, Navigate } from 'react-router-dom';
import { ROUTES } from "./constants/Routes";
import './App.css';
import Update from './components/pages/Update/Update';

function App() {
  return (
    <div className="App">
      <Routes>
        {/* Map through the object values to generate routes */}
        {Object.values(ROUTES).map((route) => (
          <Route key={route.path} path={route.path} element={route.element} />
        ))}
        <Route path="/home" element={ROUTES.HOME.element} />
        <Route path="/update/:type/:id" element={<Update />} />
        <Route path="*" element={<Navigate to={ROUTES.HOME.path} replace />} />
      </Routes>
    </div >
  );
}

export default App;
