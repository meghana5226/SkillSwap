import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { ShieldAlert, ShieldCheck, Users, BookOpenCheck, Hourglass, Star } from "lucide-react";
import { adminApi } from "../api/admin";
import { StatCard } from "../components/StatCard";
import { Skeleton } from "../components/Skeleton";
import { Button } from "../components/Button";
import { extractErrorMessage } from "../lib/errors";

type Tab = "users" | "audit";

export function AdminPage() {
  const [tab, setTab] = useState<Tab>("users");
  const queryClient = useQueryClient();

  const { data: stats, isLoading: statsLoading } = useQuery({
    queryKey: ["admin", "stats"],
    queryFn: adminApi.stats,
  });

  const { data: users, isLoading: usersLoading } = useQuery({
    queryKey: ["admin", "users"],
    queryFn: adminApi.listUsers,
    enabled: tab === "users",
  });

  const { data: auditLogs, isLoading: auditLoading } = useQuery({
    queryKey: ["admin", "audit-logs"],
    queryFn: adminApi.auditLogs,
    enabled: tab === "audit",
  });

  const toggleStatusMutation = useMutation({
    mutationFn: ({ userId, enabled }: { userId: string; enabled: boolean }) => adminApi.setUserStatus(userId, enabled),
    onSuccess: () => {
      toast.success("User status updated");
      queryClient.invalidateQueries({ queryKey: ["admin"] });
    },
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  return (
    <div className="mx-auto max-w-5xl px-6 py-10">
      <h1 className="font-display text-2xl font-bold">Admin</h1>
      <p className="mt-1 text-sm text-slate-500">Platform health, user management, and the audit trail.</p>

      {statsLoading || !stats ? (
        <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {Array.from({ length: 4 }).map((_, i) => (
            <Skeleton key={i} className="h-24 w-full" />
          ))}
        </div>
      ) : (
        <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          <StatCard label="Total users" value={stats.totalUsers} icon={Users} accent="indigo" />
          <StatCard label="Completed sessions" value={stats.completedSessions} icon={BookOpenCheck} accent="teal" />
          <StatCard label="Pending sessions" value={stats.pendingSessions} icon={Hourglass} accent="amber" />
          <StatCard
            label="Platform rating"
            value={stats.totalReviews > 0 ? `${stats.averagePlatformRating.toFixed(1)} ★` : "—"}
            icon={Star}
            accent="rose"
          />
        </div>
      )}

      <div className="mt-8 flex gap-2 border-b border-slate-200 dark:border-slate-800">
        {(["users", "audit"] as const).map((t) => (
          <button
            key={t}
            onClick={() => setTab(t)}
            className={`px-4 py-2 text-sm font-medium ${
              tab === t ? "border-b-2 border-indigo-600 text-indigo-600" : "text-slate-500"
            }`}
          >
            {t === "users" ? "User Management" : "Audit Log"}
          </button>
        ))}
      </div>

      {tab === "users" && (
        <div className="glass-card mt-6 overflow-hidden">
          {usersLoading ? (
            <div className="p-6">
              <Skeleton className="h-40 w-full" />
            </div>
          ) : (
            <table className="w-full text-left text-sm">
              <thead className="border-b border-slate-100 text-xs uppercase text-slate-400 dark:border-slate-800">
                <tr>
                  <th className="px-4 py-3">Name</th>
                  <th className="px-4 py-3">Email</th>
                  <th className="px-4 py-3">Role</th>
                  <th className="px-4 py-3">Status</th>
                  <th className="px-4 py-3"></th>
                </tr>
              </thead>
              <tbody>
                {users?.map((u) => (
                  <tr key={u.id} className="border-b border-slate-50 dark:border-slate-800/60">
                    <td className="px-4 py-3 font-medium">{u.fullName}</td>
                    <td className="px-4 py-3 text-slate-500">{u.email}</td>
                    <td className="px-4 py-3">
                      <span className="rounded-full bg-slate-100 px-2 py-0.5 text-xs dark:bg-slate-800">
                        {u.role.toLowerCase()}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      {u.enabled ? (
                        <span className="inline-flex items-center gap-1 text-emerald-600">
                          <ShieldCheck size={14} /> Active
                        </span>
                      ) : (
                        <span className="inline-flex items-center gap-1 text-rose-500">
                          <ShieldAlert size={14} /> Disabled
                        </span>
                      )}
                    </td>
                    <td className="px-4 py-3 text-right">
                      <Button
                        variant="ghost"
                        isLoading={toggleStatusMutation.isPending}
                        onClick={() => toggleStatusMutation.mutate({ userId: u.id, enabled: !u.enabled })}
                      >
                        {u.enabled ? "Disable" : "Enable"}
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {tab === "audit" && (
        <div className="glass-card mt-6 overflow-hidden">
          {auditLoading ? (
            <div className="p-6">
              <Skeleton className="h-40 w-full" />
            </div>
          ) : auditLogs?.length === 0 ? (
            <p className="p-6 text-sm text-slate-400">No admin actions logged yet.</p>
          ) : (
            <ul className="divide-y divide-slate-100 dark:divide-slate-800/60">
              {auditLogs?.map((log) => (
                <li key={log.id} className="px-4 py-3 text-sm">
                  <span className="font-medium">{log.action}</span>{" "}
                  <span className="text-slate-500">by {log.actorName}</span>
                  {log.details && <span className="text-slate-400"> — {log.details}</span>}
                  <p className="mt-0.5 text-xs text-slate-400">{new Date(log.createdAt).toLocaleString()}</p>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  );
}
