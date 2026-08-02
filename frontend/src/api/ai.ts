import { apiClient } from "./client";
import type { AiTextResponse, ChatTurn, MentorRecommendationResponse } from "../types/ai";

export const aiApi = {
  roadmap: async (targetSkill: string, currentLevel?: string) => {
    const { data } = await apiClient.post<AiTextResponse>("/ai/roadmap", { targetSkill, currentLevel });
    return data;
  },
  skillGap: async (targetRole: string) => {
    const { data } = await apiClient.post<AiTextResponse>("/ai/skill-gap", { targetRole });
    return data;
  },
  projectIdeas: async (skill: string, level?: string) => {
    const { data } = await apiClient.post<AiTextResponse>("/ai/project-ideas", { skill, level });
    return data;
  },
  resumeReview: async (resumeText: string) => {
    const { data } = await apiClient.post<AiTextResponse>("/ai/resume-review", { resumeText });
    return data;
  },
  interviewTips: async (skill: string) => {
    const { data } = await apiClient.post<AiTextResponse>("/ai/interview-tips", { skill });
    return data;
  },
  studyPlan: async (skill: string, hoursPerWeek: number) => {
    const { data } = await apiClient.post<AiTextResponse>("/ai/study-plan", { skill, hoursPerWeek });
    return data;
  },
  mentorRecommendation: async (skill?: string) => {
    const { data } = await apiClient.post<MentorRecommendationResponse>("/ai/mentor-recommendation", { skill });
    return data;
  },
  dashboardSummary: async () => {
    const { data } = await apiClient.get<AiTextResponse>("/ai/dashboard-summary");
    return data;
  },
  chat: async (message: string, history: ChatTurn[]) => {
    const { data } = await apiClient.post<AiTextResponse>("/ai/chat", { message, history });
    return data;
  },
};
