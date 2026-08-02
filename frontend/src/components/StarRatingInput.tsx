import { useState } from "react";
import { Star } from "lucide-react";

export function StarRatingInput({ value, onChange }: { value: number; onChange: (v: number) => void }) {
  const [hovered, setHovered] = useState<number | null>(null);
  const display = hovered ?? value;

  return (
    <div className="flex gap-1">
      {[1, 2, 3, 4, 5].map((n) => (
        <button
          key={n}
          type="button"
          onClick={() => onChange(n)}
          onMouseEnter={() => setHovered(n)}
          onMouseLeave={() => setHovered(null)}
          aria-label={`${n} star${n > 1 ? "s" : ""}`}
        >
          <Star size={20} className={n <= display ? "fill-amber-400 text-amber-400" : "text-slate-300 dark:text-slate-700"} />
        </button>
      ))}
    </div>
  );
}
