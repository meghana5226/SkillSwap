import { X } from "lucide-react";
import type { UserSkill } from "../types/profile";

export function SkillBadge({ skill, onRemove }: { skill: UserSkill; onRemove?: () => void }) {
  const isOffering = skill.type === "OFFERING";
  return (
    <span
      className={`inline-flex items-center gap-1.5 rounded-full px-3 py-1.5 text-sm font-medium
        ${
          isOffering
            ? "bg-emerald-100 text-emerald-700 dark:bg-emerald-500/10 dark:text-emerald-400"
            : "bg-indigo-100 text-indigo-700 dark:bg-indigo-500/10 dark:text-indigo-400"
        }`}
    >
      {skill.skillName}
      {skill.proficiency && <span className="text-xs opacity-70">· {skill.proficiency.toLowerCase()}</span>}
      {onRemove && (
        <button onClick={onRemove} className="ml-0.5 opacity-60 hover:opacity-100" aria-label={`Remove ${skill.skillName}`}>
          <X size={13} />
        </button>
      )}
    </span>
  );
}
