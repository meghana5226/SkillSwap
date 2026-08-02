import { useState, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import toast from "react-hot-toast";
import { passwordResetApi } from "../api/passwordReset";
import { TextField } from "../components/TextField";
import { Button } from "../components/Button";
import { Logo } from "../components/Logo";
import { extractErrorMessage } from "../lib/errors";

export function ForgotPassword() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setIsLoading(true);
    try {
      await passwordResetApi.forgotPassword(email);
      toast.success("If that email is registered, a reset code is on its way.");
      navigate("/reset-password", { state: { email } });
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
        <h1 className="mt-4 font-display text-2xl font-bold">Forgot your password?</h1>
        <p className="mt-1 text-sm text-slate-500">Enter your email and we'll send you a 6-digit reset code.</p>

        <div className="mt-6">
          <TextField label="Email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </div>

        <Button type="submit" isLoading={isLoading} className="mt-6 w-full">
          Send reset code
        </Button>

        <p className="mt-4 text-center text-sm text-slate-500">
          Remembered it?{" "}
          <Link to="/login" className="font-medium text-indigo-600 hover:underline">
            Back to sign in
          </Link>
        </p>
      </motion.form>
    </div>
  );
}
