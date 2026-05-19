import { Routes, Route } from "react-router-dom";

import MainLayout from "./layouts/MainLayout";
import AuthLayout from "./layouts/AuthLayout";
import DashboardLayout from "./layouts/DashboardLayout";
import RequestsPage from "./pages/requests/RequestsPage";
import ProtectedRoute from "./routes/ProtectedRoute";
import SkillsPage from "./pages/skills/SkillsPage";
import LandingPage from "./pages/landing/LandingPage";
import LoginPage from "./pages/auth/LoginPage";
import SignupPage from "./pages/auth/SignupPage";
import DashboardPage from "./pages/dashboard/DashboardPage";

function App() {

  return (

    <Routes>

      <Route
      element={<MainLayout/>}
      >

        <Route
        path="/"
        element={
          <LandingPage/>
        }
        />

      </Route>
      <Route
path="/skills"
element={
<ProtectedRoute>
<SkillsPage/>
</ProtectedRoute>
}
/>
<Route
path="/requests"
element={
<ProtectedRoute>
<RequestsPage/>
</ProtectedRoute>
}
/>


      <Route
      element={<AuthLayout/>}
      >

        <Route
        path="/login"
        element={
          <LoginPage/>
        }
        />

        <Route
        path="/signup"
        element={
          <SignupPage/>
        }
        />

      </Route>


      <Route

      element={

      <ProtectedRoute>

        <DashboardLayout/>

      </ProtectedRoute>

      }

      >

      <Route

      path="/dashboard"

      element={
        <DashboardPage/>
      }

      />

      </Route>

    </Routes>

  );

}

export default App;