import { useState, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import toast from "react-hot-toast";
import { useAuth } from "../context/AuthContext";
import { TextField } from "../components/TextField";
import { Button } from "../components/Button";
import { Logo } from "../components/Logo";
import { extractErrorMessage } from "../lib/errors";
import type { Role } from "../types/auth";

export function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState<Role>("STUDENT");
  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setIsLoading(true);
    try {
      await register({ fullName, email, password, role });
      navigate("/dashboard");
    } catch (err) {
      toast.error(extractErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <div className="brand-aura flex min-h-screen items-center justify-center px-6">
      <motion.form
        initial={{ opacity: 0, y: 12 }}
        animate={{ opacity: 1, y: 0 }}
        onSubmit={handleSubmit}
        className="glass-card w-full max-w-sm p-8 shadow-xl"
      >
        <Logo className="h-9 w-9" />
        <h1 className="mt-4 font-display text-2xl font-bold">Create your account</h1>
        <p className="mt-1 text-sm text-slate-500">Free forever. No credit card, ever.</p>

        <div className="mt-6 flex flex-col gap-4">
          <TextField label="Full name" value={fullName} onChange={(e) => setFullName(e.target.value)} required />
          <TextField label="Email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          <TextField
            label="Password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-slate-700 dark:text-slate-300">I want to join as</label>
            <select
              value={role}
              onChange={(e) => setRole(e.target.value as Role)}
              className="rounded-xl border border-slate-200 bg-white/70 px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-indigo-500 dark:border-slate-700 dark:bg-slate-900/60"
            >
              <option value="STUDENT">Student — I want to learn</option>
              <option value="MENTOR">Mentor — I want to teach</option>
            </select>
          </div>
        </div>

        <Button type="submit" isLoading={isLoading} className="mt-6 w-full">
          Create account
        </Button>

        <p className="mt-4 text-center text-sm text-slate-500">
          Already have an account?{" "}
          <Link to="/login" className="font-medium text-indigo-600 hover:underline">
            Sign in
          </Link>
        </p>
      </motion.form>
    </div>
  );
}
