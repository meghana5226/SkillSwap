import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { sessionApi } from "../api/sessions";
import { SessionCard } from "../components/SessionCard";
import { Skeleton } from "../components/Skeleton";
import { extractErrorMessage } from "../lib/errors";

type Tab = "incoming" | "outgoing";

export function SessionsPage() {
  const [tab, setTab] = useState<Tab>("incoming");
  const queryClient = useQueryClient();

  const { data: incoming, isLoading: loadingIncoming } = useQuery({
    queryKey: ["sessions", "incoming"],
    queryFn: sessionApi.incoming,
  });

  const { data: outgoing, isLoading: loadingOutgoing } = useQuery({
    queryKey: ["sessions", "outgoing"],
    queryFn: sessionApi.outgoing,
  });

  function invalidateSessions() {
    queryClient.invalidateQueries({ queryKey: ["sessions"] });
  }

  const acceptMutation = useMutation({
    mutationFn: sessionApi.accept,
    onSuccess: invalidateSessions,
    onError: (err) => toast.error(extractErrorMessage(err)),
  });
  const rejectMutation = useMutation({
    mutationFn: sessionApi.reject,
    onSuccess: invalidateSessions,
    onError: (err) => toast.error(extractErrorMessage(err)),
  });
  const completeMutation = useMutation({
    mutationFn: sessionApi.complete,
    onSuccess: invalidateSessions,
    onError: (err) => toast.error(extractErrorMessage(err)),
  });
  const cancelMutation = useMutation({
    mutationFn: sessionApi.cancel,
    onSuccess: invalidateSessions,
    onError: (err) => toast.error(extractErrorMessage(err)),
  });
  const reviewMutation = useMutation({
    mutationFn: ({ id, rating, comment }: { id: string; rating: number; comment: string }) =>
      sessionApi.review(id, { rating, comment: comment || undefined }),
    onSuccess: () => {
      toast.success("Thanks for the feedback!");
      invalidateSessions();
    },
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const isLoading = tab === "incoming" ? loadingIncoming : loadingOutgoing;
  const items = tab === "incoming" ? incoming : outgoing;
  const isActionLoading =
    acceptMutation.isPending || rejectMutation.isPending || completeMutation.isPending ||
    cancelMutation.isPending || reviewMutation.isPending;

  return (
    <div className="mx-auto max-w-3xl px-6 py-12">
      <h1 className="font-display text-2xl font-bold">Your sessions</h1>

      <div className="mt-6 flex gap-2 border-b border-slate-200 dark:border-slate-800">
        {(["incoming", "outgoing"] as const).map((t) => (
          <button
            key={t}
            onClick={() => setTab(t)}
            className={`px-4 py-2 text-sm font-medium ${
              tab === t
                ? "border-b-2 border-indigo-600 text-indigo-600"
                : "text-slate-500 hover:text-slate-700 dark:hover:text-slate-300"
            }`}
          >
            {t === "incoming" ? "Requests to me (mentor)" : "My requests (learner)"}
          </button>
        ))}
      </div>

      <div className="mt-6 flex flex-col gap-4">
        {isLoading && Array.from({ length: 3 }).map((_, i) => <Skeleton key={i} className="h-32 w-full" />)}

        {!isLoading && items?.length === 0 && (
          <p className="text-sm text-slate-500">
            {tab === "incoming" ? "No one has requested a session with you yet." : "You haven't requested any sessions yet."}
          </p>
        )}

        {items?.map((session) => (
          <SessionCard
            key={session.id}
            session={session}
            perspective={tab === "incoming" ? "mentor" : "requester"}
            isActionLoading={isActionLoading}
            onAccept={() => acceptMutation.mutate(session.id)}
            onReject={() => rejectMutation.mutate(session.id)}
            onComplete={() => completeMutation.mutate(session.id)}
            onCancel={() => cancelMutation.mutate(session.id)}
            onReview={(rating, comment) => reviewMutation.mutate({ id: session.id, rating, comment })}
          />
        ))}
      </div>
    </div>
  );
}
