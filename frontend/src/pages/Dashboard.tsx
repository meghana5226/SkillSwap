import { useQuery } from "@tanstack/react-query";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import { AreaChart, Area, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from "recharts";
import { BookOpen, GraduationCap, Hourglass, Search, Sparkles, Star, Users, Wrench } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import { dashboardApi } from "../api/dashboard";
import { StatCard } from "../components/StatCard";
import { Skeleton } from "../components/Skeleton";

const QUICK_LINKS = [
  { title: "Find a Mentor", to: "/mentors", icon: Search, desc: "Search by skill and request a session" },
  { title: "My Sessions", to: "/sessions", icon: Users, desc: "Track incoming and outgoing requests" },
  { title: "AI Tools", to: "/ai", icon: Sparkles, desc: "Roadmaps, resume review, chat & more" },
  { title: "Profile & Skills", to: "/profile", icon: Wrench, desc: "Update what you teach and want to learn" },
];

export function Dashboard() {
  const { user } = useAuth();

  const { data: stats, isLoading } = useQuery({
    queryKey: ["dashboard", "stats"],
    queryFn: dashboardApi.stats,
  });

  return (
    <div className="brand-aura">
      <div className="mx-auto max-w-6xl px-6 py-10 sm:py-12">
        <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }}>
          <h1 className="font-display text-2xl font-bold sm:text-3xl">
            Welcome back, {user?.fullName.split(" ")[0]}
          </h1>
          <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">
            Here's where your learning and mentoring stand right now.
          </p>
        </motion.div>

        {isLoading || !stats ? (
          <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {Array.from({ length: 4 }).map((_, i) => (
              <Skeleton key={i} className="h-24 w-full" />
            ))}
          </div>
        ) : (
          <motion.div
            initial={{ opacity: 0, y: 8 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.05 }}
            className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4"
          >
            <StatCard label="Skills offering" value={stats.skillsOffering} icon={GraduationCap} accent="indigo" />
            <StatCard label="Skills learning" value={stats.skillsLearning} icon={BookOpen} accent="teal" />
            <StatCard
              label="Pending requests"
              value={stats.pendingIncoming + stats.pendingOutgoing}
              icon={Hourglass}
              accent="amber"
            />
            <StatCard
              label="Rating received"
              value={stats.reviewCount > 0 ? `${stats.averageRatingReceived.toFixed(1)} ★` : "—"}
              icon={Star}
              accent="rose"
            />
          </motion.div>
        )}

        <div className="mt-8 grid gap-6 lg:grid-cols-3">
          <motion.div
            initial={{ opacity: 0, y: 8 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 }}
            className="glass-card p-6 lg:col-span-2"
          >
            <h2 className="font-display font-semibold">Session activity (last 6 months)</h2>
            <p className="text-xs text-slate-500 dark:text-slate-400">Completed sessions, as learner + mentor combined</p>
            <div className="mt-4 h-64">
              {isLoading || !stats ? (
                <Skeleton className="h-full w-full" />
              ) : (
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={stats.sessionActivity} margin={{ top: 5, right: 10, left: -20, bottom: 0 }}>
                    <defs>
                      <linearGradient id="activityGradient" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="0%" stopColor="#6366F1" stopOpacity={0.35} />
                        <stop offset="100%" stopColor="#6366F1" stopOpacity={0} />
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} className="stroke-slate-200 dark:stroke-slate-800" />
                    <XAxis dataKey="month" tick={{ fontSize: 12 }} axisLine={false} tickLine={false} />
                    <YAxis allowDecimals={false} tick={{ fontSize: 12 }} axisLine={false} tickLine={false} width={30} />
                    <Tooltip
                      contentStyle={{ borderRadius: 12, border: "1px solid #e2e8f0", fontSize: 13 }}
                      labelStyle={{ fontWeight: 600 }}
                    />
                    <Area
                      type="monotone"
                      dataKey="completedSessions"
                      name="Completed"
                      stroke="#6366F1"
                      strokeWidth={2.5}
                      fill="url(#activityGradient)"
                    />
                  </AreaChart>
                </ResponsiveContainer>
              )}
            </div>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 8 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.15 }}
            className="glass-card p-6"
          >
            <h2 className="font-display font-semibold">Breakdown</h2>
            <dl className="mt-4 space-y-3 text-sm">
              <div className="flex justify-between">
                <dt className="text-slate-500 dark:text-slate-400">Completed as learner</dt>
                <dd className="font-medium">{stats?.completedAsLearner ?? "—"}</dd>
              </div>
              <div className="flex justify-between">
                <dt className="text-slate-500 dark:text-slate-400">Completed as mentor</dt>
                <dd className="font-medium">{stats?.completedAsMentor ?? "—"}</dd>
              </div>
              <div className="flex justify-between">
                <dt className="text-slate-500 dark:text-slate-400">Requests awaiting you</dt>
                <dd className="font-medium">{stats?.pendingIncoming ?? "—"}</dd>
              </div>
              <div className="flex justify-between">
                <dt className="text-slate-500 dark:text-slate-400">Your outgoing requests</dt>
                <dd className="font-medium">{stats?.pendingOutgoing ?? "—"}</dd>
              </div>
              <div className="flex justify-between">
                <dt className="text-slate-500 dark:text-slate-400">Reviews received</dt>
                <dd className="font-medium">{stats?.reviewCount ?? "—"}</dd>
              </div>
            </dl>
          </motion.div>
        </div>

        <motion.div
          initial={{ opacity: 0, y: 8 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4"
        >
          {QUICK_LINKS.map((item) => (
            <Link
              key={item.title}
              to={item.to}
              className="glass-card group p-5 transition hover:border-indigo-300 dark:hover:border-indigo-700"
            >
              <item.icon size={20} className="text-indigo-500" />
              <h3 className="mt-3 font-display font-semibold">{item.title}</h3>
              <p className="mt-1 text-xs text-slate-500 dark:text-slate-400">{item.desc}</p>
            </Link>
          ))}
        </motion.div>
      </div>
    </div>
  );
}
