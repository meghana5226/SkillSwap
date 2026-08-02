import { useState } from "react";
import { useMutation, useQuery } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { aiApi } from "../api/ai";
import { Button } from "../components/Button";
import { TextField } from "../components/TextField";
import { MarkdownLite } from "../components/MarkdownLite";
import { Skeleton } from "../components/Skeleton";
import { extractErrorMessage } from "../lib/errors";
import type { ChatTurn } from "../types/ai";

type Tab =
  | "roadmap"
  | "skill-gap"
  | "projects"
  | "resume"
  | "interview"
  | "study-plan"
  | "mentor-match"
  | "chat";

const TABS: { id: Tab; label: string }[] = [
  { id: "roadmap", label: "Learning Roadmap" },
  { id: "skill-gap", label: "Skill Gap" },
  { id: "projects", label: "Project Ideas" },
  { id: "resume", label: "Resume Review" },
  { id: "interview", label: "Interview Tips" },
  { id: "study-plan", label: "Study Planner" },
  { id: "mentor-match", label: "Mentor Match" },
  { id: "chat", label: "Chat Assistant" },
];

function ResultBox({ content }: { content: string | null }) {
  if (!content) return null;
  return (
    <div className="mt-4 rounded-xl border border-slate-200 bg-white/70 p-4 dark:border-slate-800 dark:bg-slate-900/60">
      <MarkdownLite content={content} />
    </div>
  );
}

function AiErrorHint() {
  return (
    <p className="mt-2 text-xs text-slate-400">
      Powered by a local model via Ollama. If this fails, make sure Ollama is running
      (<code className="rounded bg-slate-100 px-1 dark:bg-slate-800">ollama serve</code>) and the model from your{" "}
      <code className="rounded bg-slate-100 px-1 dark:bg-slate-800">.env</code> is pulled.
    </p>
  );
}

export function AiHubPage() {
  const [tab, setTab] = useState<Tab>("roadmap");

  const { data: summary, isLoading: summaryLoading } = useQuery({
    queryKey: ["ai", "dashboard-summary"],
    queryFn: aiApi.dashboardSummary,
    retry: false,
  });

  const [targetSkill, setTargetSkill] = useState("");
  const [currentLevel, setCurrentLevel] = useState("");
  const roadmapMutation = useMutation({
    mutationFn: () => aiApi.roadmap(targetSkill, currentLevel || undefined),
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const [targetRole, setTargetRole] = useState("");
  const skillGapMutation = useMutation({
    mutationFn: () => aiApi.skillGap(targetRole),
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const [projectSkill, setProjectSkill] = useState("");
  const [projectLevel, setProjectLevel] = useState("");
  const projectMutation = useMutation({
    mutationFn: () => aiApi.projectIdeas(projectSkill, projectLevel || undefined),
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const [resumeText, setResumeText] = useState("");
  const resumeMutation = useMutation({
    mutationFn: () => aiApi.resumeReview(resumeText),
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const [interviewSkill, setInterviewSkill] = useState("");
  const interviewMutation = useMutation({
    mutationFn: () => aiApi.interviewTips(interviewSkill),
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const [studySkill, setStudySkill] = useState("");
  const [hoursPerWeek, setHoursPerWeek] = useState(5);
  const studyPlanMutation = useMutation({
    mutationFn: () => aiApi.studyPlan(studySkill, hoursPerWeek),
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const [matchSkill, setMatchSkill] = useState("");
  const mentorMatchMutation = useMutation({
    mutationFn: () => aiApi.mentorRecommendation(matchSkill || undefined),
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const [chatInput, setChatInput] = useState("");
  const [chatHistory, setChatHistory] = useState<ChatTurn[]>([]);
  const chatMutation = useMutation({
    mutationFn: (message: string) => aiApi.chat(message, chatHistory),
    onSuccess: (data, message) => {
      setChatHistory((prev) => [...prev, { role: "user", content: message }, { role: "assistant", content: data.content }]);
      setChatInput("");
    },
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  return (
    <div className="mx-auto max-w-3xl px-6 py-12">
      <h1 className="font-display text-2xl font-bold">AI Tools</h1>
      <p className="mt-1 text-sm text-slate-500">
        Free, powered entirely by a local model — no API keys, no data leaving your machine.
      </p>

      {summaryLoading ? (
        <Skeleton className="mt-6 h-16 w-full" />
      ) : summary?.content ? (
        <div className="mt-6 rounded-xl border border-indigo-200 bg-indigo-50/60 p-4 dark:border-indigo-900 dark:bg-indigo-950/40">
          <MarkdownLite content={summary.content} />
        </div>
      ) : null}

      <div className="mt-6 flex flex-wrap gap-2 border-b border-slate-200 pb-2 dark:border-slate-800">
        {TABS.map((t) => (
          <button
            key={t.id}
            onClick={() => setTab(t.id)}
            className={`rounded-full px-3 py-1.5 text-sm font-medium transition ${
              tab === t.id
                ? "bg-indigo-600 text-white"
                : "bg-slate-100 text-slate-600 hover:bg-slate-200 dark:bg-slate-800 dark:text-slate-400"
            }`}
          >
            {t.label}
          </button>
        ))}
      </div>

      <div className="mt-6">
        {tab === "roadmap" && (
          <div>
            <TextField label="Skill you want to learn" value={targetSkill} onChange={(e) => setTargetSkill(e.target.value)} />
            <div className="mt-3">
              <TextField
                label="Current level (optional)"
                value={currentLevel}
                onChange={(e) => setCurrentLevel(e.target.value)}
                placeholder="e.g. complete beginner"
              />
            </div>
            <Button className="mt-4" isLoading={roadmapMutation.isPending} onClick={() => roadmapMutation.mutate()} disabled={!targetSkill}>
              Generate roadmap
            </Button>
            <AiErrorHint />
            <ResultBox content={roadmapMutation.data?.content ?? null} />
          </div>
        )}

        {tab === "skill-gap" && (
          <div>
            <TextField label="Target role" value={targetRole} onChange={(e) => setTargetRole(e.target.value)} placeholder="e.g. Backend Developer" />
            <Button className="mt-4" isLoading={skillGapMutation.isPending} onClick={() => skillGapMutation.mutate()} disabled={!targetRole}>
              Analyze skill gap
            </Button>
            <AiErrorHint />
            <ResultBox content={skillGapMutation.data?.content ?? null} />
          </div>
        )}

        {tab === "projects" && (
          <div>
            <TextField label="Skill to practice" value={projectSkill} onChange={(e) => setProjectSkill(e.target.value)} />
            <div className="mt-3">
              <TextField label="Level (optional)" value={projectLevel} onChange={(e) => setProjectLevel(e.target.value)} />
            </div>
            <Button className="mt-4" isLoading={projectMutation.isPending} onClick={() => projectMutation.mutate()} disabled={!projectSkill}>
              Suggest projects
            </Button>
            <AiErrorHint />
            <ResultBox content={projectMutation.data?.content ?? null} />
          </div>
        )}

        {tab === "resume" && (
          <div>
            <label className="text-sm font-medium text-slate-700 dark:text-slate-300">Paste your resume text</label>
            <textarea
              value={resumeText}
              onChange={(e) => setResumeText(e.target.value)}
              rows={8}
              className="mt-1.5 w-full rounded-xl border border-slate-200 bg-white/70 px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-indigo-500 dark:border-slate-700 dark:bg-slate-900/60"
            />
            <Button className="mt-4" isLoading={resumeMutation.isPending} onClick={() => resumeMutation.mutate()} disabled={!resumeText}>
              Review resume
            </Button>
            <AiErrorHint />
            <ResultBox content={resumeMutation.data?.content ?? null} />
          </div>
        )}

        {tab === "interview" && (
          <div>
            <TextField label="Skill / topic" value={interviewSkill} onChange={(e) => setInterviewSkill(e.target.value)} />
            <Button className="mt-4" isLoading={interviewMutation.isPending} onClick={() => interviewMutation.mutate()} disabled={!interviewSkill}>
              Get interview tips
            </Button>
            <AiErrorHint />
            <ResultBox content={interviewMutation.data?.content ?? null} />
          </div>
        )}

        {tab === "study-plan" && (
          <div>
            <TextField label="Skill" value={studySkill} onChange={(e) => setStudySkill(e.target.value)} />
            <div className="mt-3 max-w-[160px]">
              <TextField
                label="Hours this week"
                type="number"
                min={1}
                max={60}
                value={hoursPerWeek}
                onChange={(e) => setHoursPerWeek(Number(e.target.value))}
              />
            </div>
            <Button className="mt-4" isLoading={studyPlanMutation.isPending} onClick={() => studyPlanMutation.mutate()} disabled={!studySkill}>
              Build study plan
            </Button>
            <AiErrorHint />
            <ResultBox content={studyPlanMutation.data?.content ?? null} />
          </div>
        )}

        {tab === "mentor-match" && (
          <div>
            <TextField
              label="Skill (optional — uses a skill from your profile if left blank)"
              value={matchSkill}
              onChange={(e) => setMatchSkill(e.target.value)}
            />
            <Button className="mt-4" isLoading={mentorMatchMutation.isPending} onClick={() => mentorMatchMutation.mutate()}>
              Find my best-fit mentor
            </Button>
            <AiErrorHint />
            <ResultBox content={mentorMatchMutation.data?.recommendation ?? null} />
            {mentorMatchMutation.data?.candidates && mentorMatchMutation.data.candidates.length > 0 && (
              <ul className="mt-3 flex flex-col gap-2">
                {mentorMatchMutation.data.candidates.map((c) => (
                  <li key={c.userId} className="rounded-lg border border-slate-200 px-3 py-2 text-sm dark:border-slate-800">
                    <span className="font-medium">{c.fullName}</span> — {c.skillName}
                    {c.reviewCount > 0 && ` · ${c.averageRating.toFixed(1)}★ (${c.reviewCount})`}
                  </li>
                ))}
              </ul>
            )}
          </div>
        )}

        {tab === "chat" && (
          <div>
            <div className="flex max-h-[400px] flex-col gap-3 overflow-y-auto rounded-xl border border-slate-200 bg-white/70 p-4 dark:border-slate-800 dark:bg-slate-900/60">
              {chatHistory.length === 0 && <p className="text-sm text-slate-400">Ask anything about learning, mentorship, or your career.</p>}
              {chatHistory.map((turn, i) => (
                <div key={i} className={turn.role === "user" ? "self-end max-w-[85%]" : "self-start max-w-[85%]"}>
                  <div
                    className={`rounded-2xl px-4 py-2 text-sm ${
                      turn.role === "user"
                        ? "bg-indigo-600 text-white"
                        : "bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-300"
                    }`}
                  >
                    {turn.role === "assistant" ? <MarkdownLite content={turn.content} /> : turn.content}
                  </div>
                </div>
              ))}
            </div>
            <div className="mt-3 flex gap-2">
              <input
                value={chatInput}
                onChange={(e) => setChatInput(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key === "Enter" && chatInput.trim()) chatMutation.mutate(chatInput);
                }}
                placeholder="Type a message…"
                className="flex-1 rounded-xl border border-slate-200 bg-white/70 px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-indigo-500 dark:border-slate-700 dark:bg-slate-900/60"
              />
              <Button isLoading={chatMutation.isPending} disabled={!chatInput.trim()} onClick={() => chatMutation.mutate(chatInput)}>
                Send
              </Button>
            </div>
            <AiErrorHint />
          </div>
        )}
      </div>
    </div>
  );
}
