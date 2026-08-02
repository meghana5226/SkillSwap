import { apiClient } from "./client";
import type { AppNotification } from "../types/notification";

export const notificationApi = {
  list: async () => {
    const { data } = await apiClient.get<AppNotification[]>("/notifications");
    return data;
  },
  unreadCount: async () => {
    const { data } = await apiClient.get<{ count: number }>("/notifications/unread-count");
    return data.count;
  },
  markRead: async (id: string) => {
    await apiClient.post(`/notifications/${id}/read`);
  },
  markAllRead: async () => {
    await apiClient.post("/notifications/read-all");
  },
};
