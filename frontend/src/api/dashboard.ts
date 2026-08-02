import { apiClient } from "./client";
import type { DashboardStats } from "../types/dashboard";

export const dashboardApi = {
  stats: async () => {
    const { data } = await apiClient.get<DashboardStats>("/dashboard/stats");
    return data;
  },
};
