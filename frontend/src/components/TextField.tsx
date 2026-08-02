import { type InputHTMLAttributes } from "react";

interface TextFieldProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
}

export function TextField({ label, error, id, ...rest }: TextFieldProps) {
  const inputId = id ?? label.toLowerCase().replace(/\s+/g, "-");
  return (
    <div className="flex flex-col gap-1.5">
      <label htmlFor={inputId} className="text-sm font-medium text-slate-700 dark:text-slate-300">
        {label}
      </label>
      <input
        id={inputId}
        {...rest}
        className={`rounded-xl border px-4 py-2.5 text-sm outline-none transition
          bg-white/70 dark:bg-slate-900/60 backdrop-blur
          border-slate-200 dark:border-slate-700
          focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500
          ${error ? "border-red-400 focus:ring-red-400" : ""}`}
      />
      {error && <span className="text-xs text-red-500">{error}</span>}
    </div>
  );
}
