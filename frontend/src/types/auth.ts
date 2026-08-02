export type Role = "STUDENT" | "MENTOR" | "ADMIN";

export interface AuthUser {
  userId: string;
  fullName: string;
  email: string;
  role: Role;
}

export interface AuthResponse extends AuthUser {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface RegisterPayload {
  fullName: string;
  email: string;
  password: string;
  role?: Role;
}

export interface ApiErrorBody {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  fieldErrors?: Record<string, string>;
}
