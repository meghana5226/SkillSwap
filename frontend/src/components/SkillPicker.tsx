import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { profileApi } from "../api/profile";
import { useDebouncedValue } from "../hooks/useDebouncedValue";
import { Button } from "./Button";
import type { ProficiencyLevel, SkillType } from "../types/profile";

interface SkillPickerProps {
  onAdd: (skillName: string, type: SkillType, proficiency?: ProficiencyLevel) => void;
  isAdding?: boolean;
}

const PROFICIENCIES: ProficiencyLevel[] = ["BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT"];

export function SkillPicker({ onAdd, isAdding }: SkillPickerProps) {
  const [query, setQuery] = useState("");
  const [type, setType] = useState<SkillType>("OFFERING");
  const [proficiency, setProficiency] = useState<ProficiencyLevel>("INTERMEDIATE");
  const debouncedQuery = useDebouncedValue(query, 250);

  const { data: suggestions } = useQuery({
    queryKey: ["skill-search", debouncedQuery],
    queryFn: () => profileApi.searchSkills(debouncedQuery),
    enabled: debouncedQuery.length > 0,
  });

  function handleAdd(name: string) {
    if (!name.trim()) return;
    onAdd(name.trim(), type, type === "OFFERING" ? proficiency : undefined);
    setQuery("");
  }

  return (
    <div className="rounded-xl border border-dashed border-slate-300 p-4 dark:border-slate-700">
      <div className="flex flex-wrap items-center gap-2">
        <select
          value={type}
          onChange={(e) => setType(e.target.value as SkillType)}
          className="rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm dark:border-slate-700 dark:bg-slate-900"
        >
          <option value="OFFERING">I can teach</option>
          <option value="LEARNING">I want to learn</option>
        </select>

        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="e.g. React, System Design…"
          className="min-w-[180px] flex-1 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-indigo-500 dark:border-slate-700 dark:bg-slate-900"
          onKeyDown={(e) => {
            if (e.key === "Enter") handleAdd(query);
          }}
        />

        {type === "OFFERING" && (
          <select
            value={proficiency}
            onChange={(e) => setProficiency(e.target.value as ProficiencyLevel)}
            className="rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm dark:border-slate-700 dark:bg-slate-900"
          >
            {PROFICIENCIES.map((p) => (
              <option key={p} value={p}>
                {p.charAt(0) + p.slice(1).toLowerCase()}
              </option>
            ))}
          </select>
        )}

        <Button type="button" isLoading={isAdding} onClick={() => handleAdd(query)}>
          Add
        </Button>
      </div>

      {suggestions && suggestions.length > 0 && (
        <div className="mt-2 flex flex-wrap gap-2">
          {suggestions.map((s) => (
            <button
              key={s.id}
              onClick={() => handleAdd(s.name)}
              className="rounded-full border border-slate-200 px-3 py-1 text-xs text-slate-600 hover:border-indigo-400 hover:text-indigo-600 dark:border-slate-700 dark:text-slate-400"
            >
              {s.name}
            </button>
          ))}
        </div>
      )}
    </div>
  );
}
