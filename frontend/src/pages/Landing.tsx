import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import { GraduationCap, Sparkles, Users } from "lucide-react";
import { Button } from "../components/Button";
import { Logo } from "../components/Logo";

const FEATURES = [
  {
    title: "Teach",
    desc: "Share what you know and mentor others building their careers.",
    icon: Users,
  },
  {
    title: "Learn",
    desc: "Find mentors offering the exact skill you're trying to pick up.",
    icon: GraduationCap,
  },
  {
    title: "Grow",
    desc: "AI-powered roadmaps, resume reviews and project ideas — free, via a local model.",
    icon: Sparkles,
  },
];

export function Landing() {
  return (
    <div className="brand-aura min-h-screen overflow-hidden">
      <header className="mx-auto flex max-w-6xl items-center justify-between px-6 py-6">
        <div className="flex items-center gap-2">
          <Logo className="h-8 w-8" />
          <span className="font-display text-lg font-bold">SkillSwap AI</span>
        </div>
        <div className="flex items-center gap-3">
          <Link to="/login" className="text-sm font-medium text-slate-600 hover:text-slate-900 dark:text-slate-300 dark:hover:text-white">
            Sign in
          </Link>
          <Link to="/register">
            <Button className="px-4 py-2 text-sm">Get started</Button>
          </Link>
        </div>
      </header>

      <section className="relative px-6 pb-32 pt-16 text-center sm:pt-24">
        <motion.span
          initial={{ opacity: 0, y: 8 }}
          animate={{ opacity: 1, y: 0 }}
          className="inline-flex items-center gap-1.5 rounded-full border border-indigo-200 bg-indigo-50/80 px-3 py-1 text-xs font-medium text-indigo-700 dark:border-indigo-900 dark:bg-indigo-500/10 dark:text-indigo-300"
        >
          <Sparkles size={13} /> 100% free — no subscriptions, no paid tiers, ever
        </motion.span>

        <motion.h1
          initial={{ opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.05 }}
          className="font-display mx-auto mt-6 max-w-3xl text-4xl font-bold tracking-tight sm:text-6xl"
        >
          Trade skills, not money.
        </motion.h1>
        <motion.p
          initial={{ opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.1 }}
          className="mx-auto mt-6 max-w-xl text-lg text-slate-600 dark:text-slate-300"
        >
          SkillSwap AI connects students, freshers and professionals to teach,
          learn and grow together — completely free, forever.
        </motion.p>
        <motion.div
          initial={{ opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="mt-10 flex justify-center gap-4"
        >
          <Link to="/register">
            <Button className="px-6 py-3 text-base">Get started — it's free</Button>
          </Link>
          <Link to="/login">
            <Button variant="ghost" className="px-6 py-3 text-base">
              Sign in
            </Button>
          </Link>
        </motion.div>
      </section>

      <section className="mx-auto grid max-w-5xl gap-6 px-6 pb-24 sm:grid-cols-3">
        {FEATURES.map((item, i) => (
          <motion.div
            key={item.title}
            initial={{ opacity: 0, y: 12 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.4, delay: i * 0.08 }}
            className="glass-card p-6"
          >
            <span className="flex h-10 w-10 items-center justify-center rounded-xl bg-indigo-50 text-indigo-600 dark:bg-indigo-500/10 dark:text-indigo-400">
              <item.icon size={18} />
            </span>
            <h3 className="font-display mt-4 text-lg font-semibold">{item.title}</h3>
            <p className="mt-2 text-sm text-slate-600 dark:text-slate-400">{item.desc}</p>
          </motion.div>
        ))}
      </section>
    </div>
  );
}
