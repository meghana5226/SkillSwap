import { AxiosError } from "axios";
import type { ApiErrorBody } from "../types/auth";

export function extractErrorMessage(err: unknown): string {
  if (err instanceof AxiosError) {
    const body = err.response?.data as ApiErrorBody | undefined;
    if (body?.message) return body.message;
  }
  return "Something went wrong. Please try again.";
}
