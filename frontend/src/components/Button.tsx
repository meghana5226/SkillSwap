import { type ButtonHTMLAttributes } from "react";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  isLoading?: boolean;
  variant?: "primary" | "ghost";
}

export function Button({ isLoading, variant = "primary", children, className = "", disabled, ...rest }: ButtonProps) {
  const base = "rounded-xl px-4 py-2.5 text-sm font-semibold transition disabled:opacity-60 disabled:cursor-not-allowed";
  const styles =
    variant === "primary"
      ? "bg-gradient-to-r from-indigo-600 to-violet-600 text-white shadow-lg shadow-indigo-500/20 hover:opacity-95"
      : "bg-transparent border border-slate-300 dark:border-slate-700 text-slate-700 dark:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800";

  return (
    <button className={`${base} ${styles} ${className}`} disabled={disabled || isLoading} {...rest}>
      {isLoading ? "Please wait…" : children}
    </button>
  );
}
