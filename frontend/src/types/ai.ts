import type { MentorSearchResult } from "./session";

export interface AiTextResponse {
  content: string;
}

export interface ChatTurn {
  role: "user" | "assistant";
  content: string;
}

export interface MentorRecommendationResponse {
  recommendation: string;
  candidates: MentorSearchResult[];
}
