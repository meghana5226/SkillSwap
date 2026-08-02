import { apiClient } from "./client";
import type { AdminStats, AdminUser, AuditLogEntry } from "../types/admin";

export const adminApi = {
  listUsers: async () => {
    const { data } = await apiClient.get<AdminUser[]>("/admin/users");
    return data;
  },
  setUserStatus: async (userId: string, enabled: boolean) => {
    const { data } = await apiClient.patch<AdminUser>(`/admin/users/${userId}/status`, { enabled });
    return data;
  },
  stats: async () => {
    const { data } = await apiClient.get<AdminStats>("/admin/stats");
    return data;
  },
  auditLogs: async () => {
    const { data } = await apiClient.get<AuditLogEntry[]>("/admin/audit-logs");
    return data;
  },
};
