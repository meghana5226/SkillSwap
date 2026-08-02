export type NotificationType =
  | "SESSION_REQUESTED"
  | "SESSION_ACCEPTED"
  | "SESSION_REJECTED"
  | "SESSION_COMPLETED"
  | "REVIEW_RECEIVED";

export interface AppNotification {
  id: string;
  type: NotificationType;
  message: string;
  relatedSessionId: string | null;
  isRead: boolean;
  createdAt: string;
}
