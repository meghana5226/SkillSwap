import { useState, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import toast from "react-hot-toast";
import { useAuth } from "../context/AuthContext";
import { TextField } from "../components/TextField";
import { Button } from "../components/Button";
import { Logo } from "../components/Logo";
import { extractErrorMessage } from "../lib/errors";

export function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setIsLoading(true);
    try {
      await login({ email, password });
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
        <h1 className="mt-4 font-display text-2xl font-bold">Welcome back</h1>
        <p className="mt-1 text-sm text-slate-500">Sign in to continue learning and teaching.</p>

        <div className="mt-6 flex flex-col gap-4">
          <TextField label="Email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          <div>
            <TextField
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <div className="mt-1.5 text-right">
              <Link to="/forgot-password" className="text-xs font-medium text-indigo-600 hover:underline">
                Forgot password?
              </Link>
            </div>
          </div>
        </div>

        <Button type="submit" isLoading={isLoading} className="mt-2 w-full">
          Sign in
        </Button>

        <p className="mt-4 text-center text-sm text-slate-500">
          No account yet?{" "}
          <Link to="/register" className="font-medium text-indigo-600 hover:underline">
            Create one
          </Link>
        </p>
      </motion.form>
    </div>
  );
}
