import { useState, type FormEvent } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import toast from "react-hot-toast";
import { passwordResetApi } from "../api/passwordReset";
import { TextField } from "../components/TextField";
import { Button } from "../components/Button";
import { Logo } from "../components/Logo";
import { extractErrorMessage } from "../lib/errors";

export function ResetPassword() {
  const navigate = useNavigate();
  const location = useLocation();
  const prefillEmail = (location.state as { email?: string } | null)?.email ?? "";

  const [email, setEmail] = useState(prefillEmail);
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setIsLoading(true);
    try {
      await passwordResetApi.resetPassword(email, otp, newPassword);
      toast.success("Password updated — you can sign in now.");
      navigate("/login");
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
        <h1 className="mt-4 font-display text-2xl font-bold">Reset your password</h1>
        <p className="mt-1 text-sm text-slate-500">Enter the 6-digit code we sent you and choose a new password.</p>

        <div className="mt-6 flex flex-col gap-4">
          <TextField label="Email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          <TextField
            label="6-digit code"
            value={otp}
            onChange={(e) => setOtp(e.target.value.replace(/\D/g, "").slice(0, 6))}
            inputMode="numeric"
            maxLength={6}
            required
          />
          <TextField
            label="New password"
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />
        </div>

        <Button type="submit" isLoading={isLoading} className="mt-6 w-full">
          Reset password
        </Button>

        <p className="mt-4 text-center text-sm text-slate-500">
          <Link to="/forgot-password" className="font-medium text-indigo-600 hover:underline">
            Didn't get a code? Request again
          </Link>
        </p>
      </motion.form>
    </div>
  );
}
