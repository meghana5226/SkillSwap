import { createContext, useContext, useEffect, useState, type ReactNode } from "react";
import { authApi } from "../api/auth";
import { tokenStorage } from "../api/client";
import type { AuthUser, LoginPayload, RegisterPayload } from "../types/auth";

interface AuthContextValue {
  user: AuthUser | null;
  isLoading: boolean;
  login: (payload: LoginPayload) => Promise<void>;
  register: (payload: RegisterPayload) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

const USER_KEY = "skillswap_user";

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const stored = localStorage.getItem(USER_KEY);
    const hasToken = tokenStorage.getAccessToken();
    if (stored && hasToken) {
      setUser(JSON.parse(stored));
    }
    setIsLoading(false);
  }, []);

  function persistSession(response: { accessToken: string; refreshToken: string } & AuthUser) {
    tokenStorage.setTokens(response.accessToken, response.refreshToken);
    const authUser: AuthUser = {
      userId: response.userId,
      fullName: response.fullName,
      email: response.email,
      role: response.role,
    };
    localStorage.setItem(USER_KEY, JSON.stringify(authUser));
    setUser(authUser);
  }

  async function login(payload: LoginPayload) {
    const response = await authApi.login(payload);
    persistSession(response);
  }

  async function register(payload: RegisterPayload) {
    const response = await authApi.register(payload);
    persistSession(response);
  }

  function logout() {
    tokenStorage.clear();
    localStorage.removeItem(USER_KEY);
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, isLoading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within an AuthProvider");
  return ctx;
}
