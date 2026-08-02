import type { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export function RequireAdmin({ children }: { children: ReactNode }) {
  const { user } = useAuth();

  if (!user) return null; // RequireAuth (parent) handles the redirect
  if (user.role !== "ADMIN") return <Navigate to="/dashboard" replace />;

  return <>{children}</>;
}
