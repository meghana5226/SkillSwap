import { useState } from "react";
import { Bookmark, BookmarkCheck, Star } from "lucide-react";
import { Button } from "./Button";
import type { MentorSearchResult } from "../types/session";

interface MentorCardProps {
  mentor: MentorSearchResult;
  isBookmarked: boolean;
  onToggleBookmark: () => void;
  onRequestSession: (message: string) => void;
  isRequesting?: boolean;
}

export function MentorCard({ mentor, isBookmarked, onToggleBookmark, onRequestSession, isRequesting }: MentorCardProps) {
  const [isRequestOpen, setIsRequestOpen] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <div className="glass-card p-5">
      <div className="flex items-start justify-between">
        <div>
          <h3 className="font-semibold">{mentor.fullName}</h3>
          <p className="text-sm text-slate-500">
            {mentor.skillName}
            {mentor.proficiency && ` · ${mentor.proficiency.toLowerCase()}`}
          </p>
        </div>
        <button
          onClick={onToggleBookmark}
          aria-label={isBookmarked ? "Remove bookmark" : "Bookmark this mentor"}
          className="text-slate-400 hover:text-indigo-500"
        >
          {isBookmarked ? <BookmarkCheck size={18} className="text-indigo-500" /> : <Bookmark size={18} />}
        </button>
      </div>

      {mentor.bio && <p className="mt-2 line-clamp-2 text-sm text-slate-600 dark:text-slate-400">{mentor.bio}</p>}

      <div className="mt-3 flex items-center gap-3 text-xs text-slate-500">
        {mentor.reviewCount > 0 ? (
          <span className="inline-flex items-center gap-1">
            <Star size={13} className="fill-amber-400 text-amber-400" />
            {mentor.averageRating.toFixed(1)} ({mentor.reviewCount})
          </span>
        ) : (
          <span>No reviews yet</span>
        )}
        {mentor.location && <span>· {mentor.location}</span>}
        <span className={mentor.available ? "text-emerald-600" : "text-slate-400"}>
          · {mentor.available ? "Available" : "Not available"}
        </span>
      </div>

      {!isRequestOpen ? (
        <Button variant="ghost" className="mt-4 w-full" onClick={() => setIsRequestOpen(true)}>
          Request session
        </Button>
      ) : (
        <div className="mt-4 flex flex-col gap-2">
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder={`Tell ${mentor.fullName.split(" ")[0]} what you'd like help with…`}
            rows={2}
            className="rounded-lg border border-slate-200 bg-white/70 px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-indigo-500 dark:border-slate-700 dark:bg-slate-900/60"
          />
          <div className="flex gap-2">
            <Button
              isLoading={isRequesting}
              className="flex-1"
              onClick={() => {
                onRequestSession(message);
                setIsRequestOpen(false);
                setMessage("");
              }}
            >
              Send request
            </Button>
            <Button variant="ghost" onClick={() => setIsRequestOpen(false)}>
              Cancel
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
