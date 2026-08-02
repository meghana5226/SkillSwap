import { useState } from "react";
import { Button } from "./Button";
import { StatusPill } from "./StatusPill";
import { StarRatingInput } from "./StarRatingInput";
import type { SessionRequestItem } from "../types/session";

interface SessionCardProps {
  session: SessionRequestItem;
  perspective: "mentor" | "requester";
  onAccept?: () => void;
  onReject?: () => void;
  onComplete?: () => void;
  onCancel?: () => void;
  onReview?: (rating: number, comment: string) => void;
  isActionLoading?: boolean;
}

export function SessionCard({
  session,
  perspective,
  onAccept,
  onReject,
  onComplete,
  onCancel,
  onReview,
  isActionLoading,
}: SessionCardProps) {
  const [isReviewOpen, setIsReviewOpen] = useState(false);
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState("");

  const counterpartName = perspective === "mentor" ? session.requesterName : session.mentorName;

  return (
    <div className="glass-card p-5">
      <div className="flex items-start justify-between">
        <div>
          <h3 className="font-semibold">{session.skillName}</h3>
          <p className="text-sm text-slate-500">
            {perspective === "mentor" ? "Requested by" : "Mentor"}: {counterpartName}
          </p>
        </div>
        <StatusPill status={session.status} />
      </div>

      {session.message && <p className="mt-2 text-sm text-slate-600 dark:text-slate-400">"{session.message}"</p>}

      <div className="mt-4 flex flex-wrap gap-2">
        {perspective === "mentor" && session.status === "PENDING" && (
          <>
            <Button isLoading={isActionLoading} onClick={onAccept}>
              Accept
            </Button>
            <Button variant="ghost" isLoading={isActionLoading} onClick={onReject}>
              Decline
            </Button>
          </>
        )}
        {perspective === "mentor" && session.status === "ACCEPTED" && (
          <Button isLoading={isActionLoading} onClick={onComplete}>
            Mark completed
          </Button>
        )}
        {perspective === "requester" && session.status === "PENDING" && (
          <Button variant="ghost" isLoading={isActionLoading} onClick={onCancel}>
            Cancel request
          </Button>
        )}
        {perspective === "requester" && session.status === "COMPLETED" && !session.hasReview && !isReviewOpen && (
          <Button variant="ghost" onClick={() => setIsReviewOpen(true)}>
            Leave a review
          </Button>
        )}
        {perspective === "requester" && session.hasReview && (
          <span className="text-xs text-slate-400">Review submitted</span>
        )}
      </div>

      {isReviewOpen && (
        <div className="mt-4 flex flex-col gap-2 border-t border-slate-200 pt-4 dark:border-slate-800">
          <StarRatingInput value={rating} onChange={setRating} />
          <textarea
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="How was the session?"
            rows={2}
            className="rounded-lg border border-slate-200 bg-white/70 px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-indigo-500 dark:border-slate-700 dark:bg-slate-900/60"
          />
          <div className="flex gap-2">
            <Button
              isLoading={isActionLoading}
              onClick={() => {
                onReview?.(rating, comment);
                setIsReviewOpen(false);
              }}
            >
              Submit review
            </Button>
            <Button variant="ghost" onClick={() => setIsReviewOpen(false)}>
              Cancel
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
