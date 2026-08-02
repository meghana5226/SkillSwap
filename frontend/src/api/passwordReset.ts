import { apiClient } from "./client";

export const passwordResetApi = {
  forgotPassword: async (email: string) => {
    const { data } = await apiClient.post<{ message: string }>("/auth/forgot-password", { email });
    return data;
  },
  resetPassword: async (email: string, otp: string, newPassword: string) => {
    const { data } = await apiClient.post<{ message: string }>("/auth/reset-password", { email, otp, newPassword });
    return data;
  },
};
