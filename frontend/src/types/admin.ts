export interface AdminUser {
  id: string;
  fullName: string;
  email: string;
  role: "STUDENT" | "MENTOR" | "ADMIN";
  enabled: boolean;
  emailVerified: boolean;
  createdAt: string;
}

export interface AdminStats {
  totalUsers: number;
  totalStudents: number;
  totalMentors: number;
  totalAdmins: number;
  totalSessions: number;
  pendingSessions: number;
  completedSessions: number;
  totalReviews: number;
  averagePlatformRating: number;
}

export interface AuditLogEntry {
  id: string;
  actorName: string;
  actorEmail: string;
  action: string;
  targetType: string | null;
  targetId: string | null;
  details: string | null;
  createdAt: string;
}
