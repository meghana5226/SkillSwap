export interface MonthlyActivity {
  month: string;
  completedSessions: number;
}

export interface DashboardStats {
  skillsOffering: number;
  skillsLearning: number;
  completedAsLearner: number;
  completedAsMentor: number;
  pendingIncoming: number;
  pendingOutgoing: number;
  averageRatingReceived: number;
  reviewCount: number;
  sessionActivity: MonthlyActivity[];
}
