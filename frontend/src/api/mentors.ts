import { apiClient } from "./client";
import type { Bookmark, MentorSearchResult } from "../types/session";

export const mentorApi = {
  search: async (skill: string, onlyAvailable?: boolean) => {
    const { data } = await apiClient.get<MentorSearchResult[]>("/mentors/search", {
      params: { skill, onlyAvailable },
    });
    return data;
  },
};

export const bookmarkApi = {
  list: async () => {
    const { data } = await apiClient.get<Bookmark[]>("/bookmarks");
    return data;
  },
  add: async (userId: string) => {
    const { data } = await apiClient.post<Bookmark>(`/bookmarks/${userId}`);
    return data;
  },
  remove: async (userId: string) => {
    await apiClient.delete(`/bookmarks/${userId}`);
  },
};
