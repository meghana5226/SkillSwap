import { apiClient } from "./client";
import type { CreateSessionPayload, ReviewPayload, SessionRequestItem, Review } from "../types/session";

export const sessionApi = {
  create: async (payload: CreateSessionPayload) => {
    const { data } = await apiClient.post<SessionRequestItem>("/sessions", payload);
    return data;
  },
  incoming: async () => {
    const { data } = await apiClient.get<SessionRequestItem[]>("/sessions/incoming");
    return data;
  },
  outgoing: async () => {
    const { data } = await apiClient.get<SessionRequestItem[]>("/sessions/outgoing");
    return data;
  },
  accept: async (id: string) => {
    const { data } = await apiClient.post<SessionRequestItem>(`/sessions/${id}/accept`);
    return data;
  },
  reject: async (id: string) => {
    const { data } = await apiClient.post<SessionRequestItem>(`/sessions/${id}/reject`);
    return data;
  },
  complete: async (id: string) => {
    const { data } = await apiClient.post<SessionRequestItem>(`/sessions/${id}/complete`);
    return data;
  },
  cancel: async (id: string) => {
    const { data } = await apiClient.post<SessionRequestItem>(`/sessions/${id}/cancel`);
    return data;
  },
  review: async (sessionId: string, payload: ReviewPayload) => {
    const { data } = await apiClient.post<Review>(`/sessions/${sessionId}/review`, payload);
    return data;
  },
};
