import type { SessionStatus } from "../types/session";

const STYLES: Record<SessionStatus, string> = {
  PENDING: "bg-amber-100 text-amber-700 dark:bg-amber-500/10 dark:text-amber-400",
  ACCEPTED: "bg-blue-100 text-blue-700 dark:bg-blue-500/10 dark:text-blue-400",
  REJECTED: "bg-red-100 text-red-700 dark:bg-red-500/10 dark:text-red-400",
  CANCELLED: "bg-slate-100 text-slate-500 dark:bg-slate-800 dark:text-slate-400",
  COMPLETED: "bg-emerald-100 text-emerald-700 dark:bg-emerald-500/10 dark:text-emerald-400",
};

export function StatusPill({ status }: { status: SessionStatus }) {
  return (
    <span className={`rounded-full px-2.5 py-1 text-xs font-medium ${STYLES[status]}`}>
      {status.charAt(0) + status.slice(1).toLowerCase()}
    </span>
  );
}
