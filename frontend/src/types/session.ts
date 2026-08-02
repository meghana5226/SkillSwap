import type { ProficiencyLevel } from "./profile";

export type SessionStatus = "PENDING" | "ACCEPTED" | "REJECTED" | "CANCELLED" | "COMPLETED";

export interface SessionRequestItem {
  id: string;
  requesterId: string;
  requesterName: string;
  mentorId: string;
  mentorName: string;
  skillId: string;
  skillName: string;
  status: SessionStatus;
  message: string | null;
  scheduledAt: string | null;
  createdAt: string;
  hasReview: boolean;
}

export interface CreateSessionPayload {
  mentorId: string;
  skillId: string;
  message?: string;
  scheduledAt?: string;
}

export interface ReviewPayload {
  rating: number;
  comment?: string;
}

export interface Review {
  id: string;
  sessionId: string;
  reviewerId: string;
  reviewerName: string;
  rating: number;
  comment: string | null;
  createdAt: string;
}

export interface MentorSearchResult {
  userId: string;
  fullName: string;
  bio: string | null;
  experienceLevel: string | null;
  location: string | null;
  available: boolean;
  skillId: string;
  skillName: string;
  proficiency: ProficiencyLevel | null;
  averageRating: number;
  reviewCount: number;
}

export interface Bookmark {
  id: string;
  bookmarkedUserId: string;
  bookmarkedUserName: string;
  bookmarkedUserRole: string;
  createdAt: string;
}
