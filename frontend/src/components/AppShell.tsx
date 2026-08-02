import { useState, type ReactNode } from "react";
import { Link, useLocation } from "react-router-dom";
import { LayoutDashboard, Menu, Search, ShieldCheck, Sparkles, Users, X } from "lucide-react";
import { Logo } from "./Logo";
import { NotificationBell } from "./NotificationBell";
import { UserMenu } from "./UserMenu";
import { useAuth } from "../context/AuthContext";

interface NavItem {
  to: string;
  label: string;
  icon: React.ComponentType<{ size?: number }>;
}

const NAV_ITEMS: NavItem[] = [
  { to: "/dashboard", label: "Dashboard", icon: LayoutDashboard },
  { to: "/mentors", label: "Find a Mentor", icon: Search },
  { to: "/sessions", label: "Sessions", icon: Users },
  { to: "/ai", label: "AI Tools", icon: Sparkles },
];

export function AppShell({ children }: { children: ReactNode }) {
  const { user } = useAuth();
  const location = useLocation();
  const [isMobileNavOpen, setIsMobileNavOpen] = useState(false);

  const navItems = user?.role === "ADMIN" ? [...NAV_ITEMS, { to: "/admin", label: "Admin", icon: ShieldCheck }] : NAV_ITEMS;

  const SidebarContent = (
    <div className="flex h-full flex-col">
      <Link to="/dashboard" className="flex items-center gap-2 px-5 py-5">
        <Logo className="h-8 w-8" />
        <span className="font-display text-lg font-bold">SkillSwap AI</span>
      </Link>

      <nav className="mt-2 flex-1 space-y-1 px-3">
        {navItems.map((item) => {
          const isActive = location.pathname === item.to;
          const Icon = item.icon;
          return (
            <Link
              key={item.to}
              to={item.to}
              onClick={() => setIsMobileNavOpen(false)}
              className={`flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium transition ${
                isActive
                  ? "bg-indigo-50 text-indigo-700 dark:bg-indigo-500/10 dark:text-indigo-400"
                  : "text-slate-600 hover:bg-slate-100 dark:text-slate-400 dark:hover:bg-slate-800"
              }`}
            >
              <Icon size={18} />
              {item.label}
            </Link>
          );
        })}
      </nav>

      <div className="px-5 py-4 text-xs text-slate-400">
        100% free · powered by local AI
      </div>
    </div>
  );

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950">
      {/* Desktop sidebar */}
      <aside className="fixed inset-y-0 left-0 z-20 hidden w-64 border-r border-slate-200 bg-white/80 backdrop-blur-md dark:border-slate-800 dark:bg-slate-900/60 lg:block">
        {SidebarContent}
      </aside>

      {/* Mobile sidebar overlay */}
      {isMobileNavOpen && (
        <div className="fixed inset-0 z-40 lg:hidden">
          <div className="absolute inset-0 bg-black/40" onClick={() => setIsMobileNavOpen(false)} />
          <aside className="absolute inset-y-0 left-0 w-64 bg-white shadow-xl dark:bg-slate-900">
            <button
              onClick={() => setIsMobileNavOpen(false)}
              className="absolute right-3 top-4 rounded-full p-1.5 text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800"
            >
              <X size={18} />
            </button>
            {SidebarContent}
          </aside>
        </div>
      )}

      <div className="lg:pl-64">
        <header className="sticky top-0 z-10 flex items-center justify-between border-b border-slate-200 bg-white/70 px-4 py-3 backdrop-blur-md dark:border-slate-800 dark:bg-slate-950/70 sm:px-6">
          <button
            onClick={() => setIsMobileNavOpen(true)}
            className="rounded-lg p-2 text-slate-500 hover:bg-slate-100 dark:text-slate-400 dark:hover:bg-slate-800 lg:hidden"
          >
            <Menu size={20} />
          </button>
          <div className="hidden lg:block" />
          <div className="flex items-center gap-2">
            <NotificationBell />
            <UserMenu />
          </div>
        </header>

        <main>{children}</main>
      </div>
    </div>
  );
}
