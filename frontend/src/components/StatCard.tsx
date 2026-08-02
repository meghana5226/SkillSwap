import type { ComponentType } from "react";

interface StatCardProps {
  label: string;
  value: string | number;
  icon: ComponentType<{ size?: number; className?: string }>;
  accent?: "indigo" | "teal" | "amber" | "rose";
}

const ACCENTS = {
  indigo: "bg-indigo-50 text-indigo-600 dark:bg-indigo-500/10 dark:text-indigo-400",
  teal: "bg-teal-50 text-teal-600 dark:bg-teal-500/10 dark:text-teal-400",
  amber: "bg-amber-50 text-amber-600 dark:bg-amber-500/10 dark:text-amber-400",
  rose: "bg-rose-50 text-rose-600 dark:bg-rose-500/10 dark:text-rose-400",
};

export function StatCard({ label, value, icon: Icon, accent = "indigo" }: StatCardProps) {
  return (
    <div className="glass-card flex items-center gap-4 p-5">
      <span className={`flex h-11 w-11 shrink-0 items-center justify-center rounded-xl ${ACCENTS[accent]}`}>
        <Icon size={20} />
      </span>
      <div>
        <p className="text-2xl font-semibold font-display">{value}</p>
        <p className="text-xs text-slate-500 dark:text-slate-400">{label}</p>
      </div>
    </div>
  );
}
