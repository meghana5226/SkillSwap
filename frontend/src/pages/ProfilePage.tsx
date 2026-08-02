import { useEffect, useRef, useState, type FormEvent } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { motion } from "framer-motion";
import { profileApi } from "../api/profile";
import { TextField } from "../components/TextField";
import { Button } from "../components/Button";
import { Skeleton } from "../components/Skeleton";
import { SkillBadge } from "../components/SkillBadge";
import { SkillPicker } from "../components/SkillPicker";
import { extractErrorMessage } from "../lib/errors";
import type { ProficiencyLevel, SkillType, UpdateProfilePayload } from "../types/profile";

export function ProfilePage() {
  const queryClient = useQueryClient();
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [form, setForm] = useState<UpdateProfilePayload | null>(null);

  const { data: profile, isLoading } = useQuery({
    queryKey: ["profile", "me"],
    queryFn: profileApi.getMe,
  });

  // Seed the editable form once the profile has loaded, without clobbering
  // in-progress edits on background refetches.
  useEffect(() => {
    if (profile && !form) {
      setForm({
        bio: profile.bio ?? "",
        experienceLevel: profile.experienceLevel ?? "",
        githubUrl: profile.githubUrl ?? "",
        linkedinUrl: profile.linkedinUrl ?? "",
        portfolioUrl: profile.portfolioUrl ?? "",
        location: profile.location ?? "",
        available: profile.available,
      });
    }
  }, [profile, form]);

  const updateMutation = useMutation({
    mutationFn: (payload: UpdateProfilePayload) => profileApi.update(payload),
    onSuccess: () => {
      toast.success("Profile updated");
      queryClient.invalidateQueries({ queryKey: ["profile", "me"] });
    },
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const resumeMutation = useMutation({
    mutationFn: (file: File) => profileApi.uploadResume(file),
    onSuccess: () => {
      toast.success("Resume uploaded");
      queryClient.invalidateQueries({ queryKey: ["profile", "me"] });
    },
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const addSkillMutation = useMutation({
    mutationFn: (payload: { skillName: string; type: SkillType; proficiency?: ProficiencyLevel }) =>
      profileApi.addSkill(payload),
    onSuccess: () => {
      toast.success("Skill added");
      queryClient.invalidateQueries({ queryKey: ["profile", "me"] });
    },
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const removeSkillMutation = useMutation({
    mutationFn: (userSkillId: string) => profileApi.removeSkill(userSkillId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["profile", "me"] });
    },
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (form) updateMutation.mutate(form);
  }

  if (isLoading || !profile || !form) {
    return (
      <div className="mx-auto max-w-3xl px-6 py-12">
        <Skeleton className="h-8 w-48" />
        <Skeleton className="mt-4 h-24 w-full" />
        <Skeleton className="mt-4 h-24 w-full" />
      </div>
    );
  }

  const offering = profile.skills.filter((s) => s.type === "OFFERING");
  const learning = profile.skills.filter((s) => s.type === "LEARNING");

  return (
    <div className="mx-auto max-w-3xl px-6 py-12">
      <h1 className="font-display text-2xl font-bold">Your Profile</h1>
      <p className="mt-1 text-sm text-slate-500">
        {profile.fullName} · {profile.email} · {profile.role.toLowerCase()}
      </p>

      <motion.form
        initial={{ opacity: 0, y: 8 }}
        animate={{ opacity: 1, y: 0 }}
        onSubmit={handleSubmit}
        className="mt-8 flex flex-col gap-4 glass-card p-6"
      >
        <div className="flex flex-col gap-1.5">
          <label className="text-sm font-medium text-slate-700 dark:text-slate-300">Bio</label>
          <textarea
            value={form.bio}
            onChange={(e) => setForm({ ...form, bio: e.target.value })}
            rows={3}
            maxLength={1000}
            className="rounded-xl border border-slate-200 bg-white/70 px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-indigo-500 dark:border-slate-700 dark:bg-slate-900/60"
          />
        </div>

        <TextField
          label="Experience level"
          value={form.experienceLevel}
          onChange={(e) => setForm({ ...form, experienceLevel: e.target.value })}
          placeholder="Beginner / Intermediate / Senior…"
        />
        <TextField
          label="Location (optional)"
          value={form.location}
          onChange={(e) => setForm({ ...form, location: e.target.value })}
        />
        <TextField
          label="GitHub URL"
          value={form.githubUrl}
          onChange={(e) => setForm({ ...form, githubUrl: e.target.value })}
        />
        <TextField
          label="LinkedIn URL"
          value={form.linkedinUrl}
          onChange={(e) => setForm({ ...form, linkedinUrl: e.target.value })}
        />
        <TextField
          label="Portfolio URL"
          value={form.portfolioUrl}
          onChange={(e) => setForm({ ...form, portfolioUrl: e.target.value })}
        />

        <label className="flex items-center gap-2 text-sm">
          <input
            type="checkbox"
            checked={form.available}
            onChange={(e) => setForm({ ...form, available: e.target.checked })}
          />
          Available for new sessions right now
        </label>

        <Button type="submit" isLoading={updateMutation.isPending} className="self-start">
          Save changes
        </Button>
      </motion.form>

      <div className="mt-6 glass-card p-6">
        <h2 className="font-semibold">Resume</h2>
        {profile.resumeUrl ? (
          <a
            href={profile.resumeUrl}
            target="_blank"
            rel="noreferrer"
            className="mt-2 block text-sm text-indigo-600 hover:underline"
          >
            View current resume
          </a>
        ) : (
          <p className="mt-2 text-sm text-slate-500">No resume uploaded yet.</p>
        )}
        <input
          ref={fileInputRef}
          type="file"
          accept=".pdf,.doc,.docx"
          className="hidden"
          onChange={(e) => {
            const file = e.target.files?.[0];
            if (file) resumeMutation.mutate(file);
          }}
        />
        <Button
          type="button"
          variant="ghost"
          className="mt-3"
          isLoading={resumeMutation.isPending}
          onClick={() => fileInputRef.current?.click()}
        >
          {profile.resumeUrl ? "Replace resume" : "Upload resume"}
        </Button>
      </div>

      <div className="mt-6 glass-card p-6">
        <h2 className="font-semibold">Skills</h2>

        <div className="mt-3">
          <h3 className="text-xs font-medium uppercase tracking-wide text-slate-500">I can teach</h3>
          <div className="mt-2 flex flex-wrap gap-2">
            {offering.length === 0 && <p className="text-sm text-slate-400">Nothing added yet.</p>}
            {offering.map((s) => (
              <SkillBadge key={s.id} skill={s} onRemove={() => removeSkillMutation.mutate(s.id)} />
            ))}
          </div>
        </div>

        <div className="mt-4">
          <h3 className="text-xs font-medium uppercase tracking-wide text-slate-500">I want to learn</h3>
          <div className="mt-2 flex flex-wrap gap-2">
            {learning.length === 0 && <p className="text-sm text-slate-400">Nothing added yet.</p>}
            {learning.map((s) => (
              <SkillBadge key={s.id} skill={s} onRemove={() => removeSkillMutation.mutate(s.id)} />
            ))}
          </div>
        </div>

        <div className="mt-4">
          <SkillPicker
            isAdding={addSkillMutation.isPending}
            onAdd={(skillName, type, proficiency) => addSkillMutation.mutate({ skillName, type, proficiency })}
          />
        </div>
      </div>
    </div>
  );
}
