import { apiClient } from "./client";
import type { AddSkillPayload, Profile, Skill, UpdateProfilePayload } from "../types/profile";

export const profileApi = {
  getMe: async () => {
    const { data } = await apiClient.get<Profile>("/profile/me");
    return data;
  },
  update: async (payload: UpdateProfilePayload) => {
    const { data } = await apiClient.put<Profile>("/profile/me", payload);
    return data;
  },
  uploadResume: async (file: File) => {
    const formData = new FormData();
    formData.append("file", file);
    const { data } = await apiClient.post<{ resumeUrl: string }>("/profile/me/resume", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return data;
  },
  addSkill: async (payload: AddSkillPayload) => {
    const { data } = await apiClient.post("/profile/me/skills", payload);
    return data;
  },
  removeSkill: async (userSkillId: string) => {
    await apiClient.delete(`/profile/me/skills/${userSkillId}`);
  },
  searchSkills: async (query: string) => {
    const { data } = await apiClient.get<Skill[]>("/profile/skills/search", { params: { query } });
    return data;
  },
};
