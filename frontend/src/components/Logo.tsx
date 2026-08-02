export function Logo({ className = "h-8 w-8" }: { className?: string }) {
  return (
    <svg viewBox="0 0 32 32" className={className} fill="none" xmlns="http://www.w3.org/2000/svg">
      <defs>
        <linearGradient id="logoGradient" x1="0" y1="0" x2="32" y2="32" gradientUnits="userSpaceOnUse">
          <stop stopColor="#6366F1" />
          <stop offset="1" stopColor="#14B8A6" />
        </linearGradient>
      </defs>
      <rect width="32" height="32" rx="9" fill="url(#logoGradient)" />
      <path
        d="M10 13.5c0-1.933 1.567-3.5 3.5-3.5H19a3 3 0 0 1 3 3v.5"
        stroke="white"
        strokeWidth="2"
        strokeLinecap="round"
      />
      <path d="M20.5 11.5 22.5 13.5 20.5 15.5" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
      <path
        d="M22 18.5c0 1.933-1.567 3.5-3.5 3.5H13a3 3 0 0 1-3-3v-.5"
        stroke="white"
        strokeWidth="2"
        strokeLinecap="round"
      />
      <path d="M11.5 20.5 9.5 18.5l2-2" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}
