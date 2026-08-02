import { useRef, useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Bell } from "lucide-react";
import { notificationApi } from "../api/notifications";
import { useOnClickOutside } from "../hooks/useOnClickOutside";

const TYPE_ICON: Record<string, string> = {
  SESSION_REQUESTED: "📩",
  SESSION_ACCEPTED: "✅",
  SESSION_REJECTED: "❌",
  SESSION_COMPLETED: "🎉",
  REVIEW_RECEIVED: "⭐",
};

function timeAgo(iso: string): string {
  const seconds = Math.floor((Date.now() - new Date(iso).getTime()) / 1000);
  if (seconds < 60) return "just now";
  const minutes = Math.floor(seconds / 60);
  if (minutes < 60) return `${minutes}m ago`;
  const hours = Math.floor(minutes / 60);
  if (hours < 24) return `${hours}h ago`;
  return `${Math.floor(hours / 24)}d ago`;
}

export function NotificationBell() {
  const [isOpen, setIsOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);
  useOnClickOutside(ref, () => setIsOpen(false));
  const queryClient = useQueryClient();

  const { data: unreadCount } = useQuery({
    queryKey: ["notifications", "unread-count"],
    queryFn: notificationApi.unreadCount,
    refetchInterval: 30_000,
  });

  const { data: notifications } = useQuery({
    queryKey: ["notifications", "list"],
    queryFn: notificationApi.list,
    enabled: isOpen,
  });

  const markAllReadMutation = useMutation({
    mutationFn: notificationApi.markAllRead,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["notifications"] });
    },
  });

  return (
    <div className="relative" ref={ref}>
      <button
        onClick={() => setIsOpen((v) => !v)}
        className="relative rounded-full p-2 text-slate-500 hover:bg-slate-100 dark:text-slate-400 dark:hover:bg-slate-800"
        aria-label="Notifications"
      >
        <Bell size={20} />
        {!!unreadCount && unreadCount > 0 && (
          <span className="absolute -right-0.5 -top-0.5 flex h-4 w-4 items-center justify-center rounded-full bg-rose-500 text-[10px] font-semibold text-white">
            {unreadCount > 9 ? "9+" : unreadCount}
          </span>
        )}
      </button>

      {isOpen && (
        <div className="absolute right-0 z-30 mt-2 w-80 overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-xl dark:border-slate-800 dark:bg-slate-900">
          <div className="flex items-center justify-between border-b border-slate-100 px-4 py-3 dark:border-slate-800">
            <span className="text-sm font-semibold">Notifications</span>
            {!!unreadCount && unreadCount > 0 && (
              <button
                onClick={() => markAllReadMutation.mutate()}
                className="text-xs font-medium text-indigo-600 hover:underline"
              >
                Mark all read
              </button>
            )}
          </div>
          <div className="max-h-96 overflow-y-auto">
            {!notifications || notifications.length === 0 ? (
              <p className="px-4 py-6 text-center text-sm text-slate-400">You're all caught up.</p>
            ) : (
              notifications.map((n) => (
                <div
                  key={n.id}
                  className={`flex gap-2 border-b border-slate-50 px-4 py-3 text-sm dark:border-slate-800/60 ${
                    n.isRead ? "opacity-60" : ""
                  }`}
                >
                  <span>{TYPE_ICON[n.type] ?? "🔔"}</span>
                  <div>
                    <p className="text-slate-700 dark:text-slate-300">{n.message}</p>
                    <p className="mt-0.5 text-xs text-slate-400">{timeAgo(n.createdAt)}</p>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      )}
    </div>
  );
}
