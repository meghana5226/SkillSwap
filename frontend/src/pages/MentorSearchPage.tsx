import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { mentorApi, bookmarkApi } from "../api/mentors";
import { sessionApi } from "../api/sessions";
import { MentorCard } from "../components/MentorCard";
import { Skeleton } from "../components/Skeleton";
import { useDebouncedValue } from "../hooks/useDebouncedValue";
import { extractErrorMessage } from "../lib/errors";

export function MentorSearchPage() {
  const queryClient = useQueryClient();
  const [query, setQuery] = useState("");
  const [onlyAvailable, setOnlyAvailable] = useState(false);
  const debouncedQuery = useDebouncedValue(query, 300);

  const { data: mentors, isLoading } = useQuery({
    queryKey: ["mentors", "search", debouncedQuery, onlyAvailable],
    queryFn: () => mentorApi.search(debouncedQuery, onlyAvailable || undefined),
  });

  const { data: bookmarks } = useQuery({
    queryKey: ["bookmarks"],
    queryFn: bookmarkApi.list,
  });

  const bookmarkedIds = new Set((bookmarks ?? []).map((b) => b.bookmarkedUserId));

  const toggleBookmarkMutation = useMutation({
    mutationFn: async ({ userId, isBookmarked }: { userId: string; isBookmarked: boolean }) => {
      if (isBookmarked) {
        await bookmarkApi.remove(userId);
      } else {
        await bookmarkApi.add(userId);
      }
    },
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["bookmarks"] }),
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  const requestSessionMutation = useMutation({
    mutationFn: ({ mentorId, skillId, message }: { mentorId: string; skillId: string; message: string }) =>
      sessionApi.create({ mentorId, skillId, message: message || undefined }),
    onSuccess: () => {
      toast.success("Request sent!");
      queryClient.invalidateQueries({ queryKey: ["sessions", "outgoing"] });
    },
    onError: (err) => toast.error(extractErrorMessage(err)),
  });

  return (
    <div className="mx-auto max-w-5xl px-6 py-12">
      <h1 className="font-display text-2xl font-bold">Find a mentor</h1>
      <p className="mt-1 text-sm text-slate-500">Search by skill and send a session request.</p>

      <div className="mt-6 flex flex-wrap items-center gap-3">
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="e.g. React, System Design, Docker…"
          className="min-w-[240px] flex-1 rounded-xl border border-slate-200 bg-white/70 px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-indigo-500 dark:border-slate-700 dark:bg-slate-900/60"
        />
        <label className="flex items-center gap-2 text-sm text-slate-600 dark:text-slate-400">
          <input type="checkbox" checked={onlyAvailable} onChange={(e) => setOnlyAvailable(e.target.checked)} />
          Only show available mentors
        </label>
      </div>

      <div className="mt-6 grid gap-4 sm:grid-cols-2">
        {isLoading &&
          Array.from({ length: 4 }).map((_, i) => <Skeleton key={i} className="h-40 w-full" />)}

        {!isLoading && mentors?.length === 0 && (
          <p className="col-span-2 text-sm text-slate-500">
            No mentors found{query ? ` for "${query}"` : ""}. Try a different search.
          </p>
        )}

        {mentors?.map((mentor) => (
          <MentorCard
            key={`${mentor.userId}-${mentor.skillId}`}
            mentor={mentor}
            isBookmarked={bookmarkedIds.has(mentor.userId)}
            onToggleBookmark={() =>
              toggleBookmarkMutation.mutate({ userId: mentor.userId, isBookmarked: bookmarkedIds.has(mentor.userId) })
            }
            isRequesting={requestSessionMutation.isPending}
            onRequestSession={(message) =>
              requestSessionMutation.mutate({ mentorId: mentor.userId, skillId: mentor.skillId, message })
            }
          />
        ))}
      </div>
    </div>
  );
}
