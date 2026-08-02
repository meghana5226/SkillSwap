# SkillSwap AI

A free, no-subscriptions platform where students, freshers and professionals
exchange technical skills — with lightweight AI features (roadmaps, resume
review, mentor matching, etc.) powered entirely by **local, free models via
Ollama**. No OpenAI/Gemini API keys required, ever.

> **Status:** Feature-complete against the original product spec. Auth
> (including forgot-password OTP), full profile management, the skill-exchange
> loop, all 10 AI features, notifications, an admin panel with audit
> logging, and a Learning Dashboard with charts are all implemented
> end-to-end (backend + frontend + DB schema + Docker). See
> [What's here / What's next](#whats-here--whats-next) for the honest
> state of each piece.

---

## Tech Stack

| Layer      | Tech |
|------------|------|
| Backend    | Java 21, Spring Boot 3, Spring Security, JWT, Spring Data JPA, PostgreSQL, Redis, Flyway |
| Frontend   | React 19, Vite, TypeScript, Tailwind CSS v4, React Router, Axios, React Query, Framer Motion |
| AI         | Ollama running locally (Qwen 2.5 / Llama 3.2 / Phi-3 Mini / DeepSeek-R1 Distill) |
| Infra      | Docker, Docker Compose |
| Deployment | Backend → Render · Frontend → Vercel · DB → Neon/Supabase · Images → Cloudinary |

---

## Design System

- **Typography**: Sora for headings (`.font-display` / `h1`–`h4`), Inter for
  body text — loaded via Google Fonts in `index.html`.
- **Brand mark**: a custom interlocking-arrows SVG logo (`src/components/Logo.tsx`)
  representing the two-way exchange the platform is built around.
- **Cards**: a single shared `.glass-card` utility (defined in `index.css`)
  keeps every card — dashboard, profile, mentor, session, admin — visually
  consistent instead of each page inventing its own border/shadow/blur combo.
- **Dark mode**: real toggle (not just OS-preference), persisted to
  `localStorage`, in the user menu.
- **Charts**: `recharts` for the Learning Dashboard's session-activity area
  chart — the one place raw data visualization earns its keep over a stat card.
- **Layout**: a persistent sidebar `AppShell` (desktop) / slide-over (mobile)
  replaces page-by-page navigation buttons, with a notification bell and
  user menu in a sticky top bar.

---

## Project Structure

```
skillswap-ai/
├── backend/                # Spring Boot API
│   ├── src/main/java/com/skillswap/
│   │   ├── config/         # Security & CORS config
│   │   ├── security/       # JWT filter, JwtService, UserDetailsService
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # Spring Data repositories
│   │   ├── dto/             # Request/response records
│   │   ├── controller/     # REST controllers
│   │   ├── service/         # Business logic
│   │   └── exception/       # Centralized error handling
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/    # Flyway SQL migrations (versioned schema)
│   └── Dockerfile
├── frontend/                # React + Vite app
│   └── src/
│       ├── api/             # Axios client + endpoint calls
│       ├── context/          # AuthContext (global auth state)
│       ├── pages/            # Landing, Login, Register, Dashboard
│       ├── components/       # Reusable UI (Button, TextField, RequireAuth)
│       └── types/
├── postman/                  # Postman collection
├── docker-compose.yml
└── .env.example
```

---

## Running Locally with Docker (recommended)

1. **Copy env template:**
   ```bash
   cp .env.example .env
   ```
   Edit `.env` and set a strong `JWT_SECRET` (e.g. `openssl rand -base64 48`).

2. **Install Ollama and pull a model** (see [AI Setup](#ai-setup-ollama) below)
   — the backend calls Ollama on your host machine, not inside Docker.

3. **Start everything:**
   ```bash
   docker compose up --build
   ```
   This starts PostgreSQL, Redis, the Spring Boot backend (`:8080`), and the
   frontend served via nginx (`:3000`).

4. Open:
   - Frontend: http://localhost:3000
   - Backend Swagger UI: http://localhost:8080/swagger-ui.html

---

## Running Locally without Docker (dev mode)

**Backend:**
```bash
cd backend
# requires a local PostgreSQL running with a `skillswap` DB (see .env.example)
mvn spring-boot:run
```

**Frontend:**
```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```
Frontend dev server runs at http://localhost:5173.

**Run backend tests:**
```bash
cd backend
mvn test
```

---

## AI Setup (Ollama)

All AI features run against a **local Ollama server** — completely free, no
API keys, and your data never leaves your machine.

1. **Install Ollama:** https://ollama.com/download (macOS, Windows, Linux).

2. **Pull a model.** Any of these work well for the AI features in this app:
   ```bash
   ollama pull qwen2.5:7b        # recommended default — good quality/speed balance
   ollama pull llama3.2          # smaller, faster, less capable
   ollama pull phi3:mini         # very lightweight, runs on modest hardware
   ollama pull deepseek-r1:7b    # stronger reasoning, slower
   ```

3. **Start Ollama** (it runs a local REST API on `http://localhost:11434`):
   ```bash
   ollama serve
   ```

4. **Point the backend at it** — already the default in `application.yml`:
   ```yaml
   app.ai.ollama.base-url: http://localhost:11434
   app.ai.ollama.model: qwen2.5:7b
   ```

### Switching models

Changing the model requires editing **one property** — no code changes:

- Local dev: `OLLAMA_MODEL` env var, or `app.ai.ollama.model` in `application.yml`
- Docker Compose: `OLLAMA_MODEL` in your root `.env`

---

## Environment Variables

See [`.env.example`](./.env.example) for the full list used by Docker Compose,
and [`backend/src/main/resources/application.yml`](./backend/src/main/resources/application.yml)
for how each maps to a Spring property. Key ones:

| Variable | Purpose |
|---|---|
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | PostgreSQL connection |
| `JWT_SECRET` | Signing key for access/refresh tokens — **must** be changed in any real deployment |
| `JWT_ACCESS_EXPIRY_MS` / `JWT_REFRESH_EXPIRY_MS` | Token lifetimes (defaults: 15 min / 7 days) |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed frontend origins |
| `OLLAMA_BASE_URL` / `OLLAMA_MODEL` | Local AI model config |
| `MAIL_HOST/PORT/USERNAME/PASSWORD` | Outgoing mail for OTP / password reset |

---

## API Docs

- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`
- Postman collection: [`postman/SkillSwap-AI.postman_collection.json`](./postman/SkillSwap-AI.postman_collection.json)

Currently documented: `POST /api/auth/register`, `POST /api/auth/login`,
`POST /api/auth/refresh`, `POST /api/auth/forgot-password`,
`POST /api/auth/reset-password`, `GET/PUT /api/profile/me`,
`GET /api/profile/{userId}`, `POST /api/profile/me/resume`,
`POST/DELETE /api/profile/me/skills`, `GET /api/profile/skills/search`,
`GET /api/mentors/search`, `GET /api/mentors/{mentorId}/reviews`,
`POST /api/sessions`, `GET /api/sessions/incoming`,
`GET /api/sessions/outgoing`,
`POST /api/sessions/{id}/accept|reject|complete|cancel`,
`POST /api/sessions/{id}/review`, `GET/POST/DELETE /api/bookmarks`,
`POST /api/ai/roadmap|skill-gap|project-ideas|resume-review|interview-tips|study-plan|mentor-recommendation|chat`,
`GET /api/ai/dashboard-summary`, `GET /api/notifications`,
`GET /api/notifications/unread-count`,
`POST /api/notifications/{id}/read`, `POST /api/notifications/read-all`,
`GET /api/dashboard/stats`, `GET /api/admin/users`,
`PATCH /api/admin/users/{userId}/status`, `GET /api/admin/stats`,
`GET /api/admin/audit-logs`.

---

## AI Features

All ten AI features from the product spec are implemented, backed entirely
by your local Ollama model (see [AI Setup](#ai-setup-ollama) above). Every
feature builds its prompt from the platform's own data (your profile,
skills, and session history) rather than asking you to re-type context:

| Feature | Endpoint | Notes |
|---|---|---|
| Learning Roadmap Generator | `POST /api/ai/roadmap` | Uses your current OFFERING skills as context |
| Skill Gap Analysis | `POST /api/ai/skill-gap` | Compares your skills against a target role |
| Project Suggestions | `POST /api/ai/project-ideas` | Given a skill + level |
| Resume Review | `POST /api/ai/resume-review` | Takes **pasted resume text**, not the uploaded file directly (see note below) |
| Tech Interview Tips | `POST /api/ai/interview-tips` | Given a skill/topic |
| Weekly Study Planner | `POST /api/ai/study-plan` | Given a skill + hours available |
| Mentor Recommendation + Smart Skill Matching | `POST /api/ai/mentor-recommendation` | DB does the actual matching/ranking; AI explains the top pick(s) — it never invents mentors outside the real search results |
| Personalized Dashboard Summary | `GET /api/ai/dashboard-summary` | Built from your profile + session counts |
| Chat Assistant | `POST /api/ai/chat` | Free-form conversation, client keeps the history |

**All prompt text lives in one file** —
[`backend/.../service/ai/PromptTemplates.java`](./backend/src/main/java/com/skillswap/service/ai/PromptTemplates.java) —
so tuning any feature's tone/output never requires touching business logic.

**Note on Resume Review:** it currently takes pasted resume text rather than
parsing the uploaded PDF/DOCX automatically. Wiring "extract text from the
uploaded resume, then review it" is a natural follow-up and doesn't require
any new AI logic — just a PDF/DOCX text-extraction step feeding the existing
endpoint.

**If Ollama isn't running or the model isn't pulled**, every AI endpoint
returns a clear `503`/`502` error explaining exactly what to do (start
`ollama serve`, or `ollama pull <model>`) rather than a generic failure.

---

## File Storage (Resume Uploads)

Resume uploads go through a `StorageService` interface with two
implementations, chosen by one config property (`app.storage.provider` /
`STORAGE_PROVIDER`):

- **`local`** (default) — saves files to a local `uploads/` folder, served
  back at `/files/**`. Zero setup, works out of the box for dev/demo.
- **`cloudinary`** (production) — uploads via Cloudinary's signed REST API.
  Set `STORAGE_PROVIDER=cloudinary` plus `CLOUDINARY_CLOUD_NAME`,
  `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`.

No code changes needed to switch between them.

---

## Password Reset (Forgot Password)

`POST /api/auth/forgot-password` → `POST /api/auth/reset-password`. A
6-digit OTP is generated and stored in **Redis** with a 10-minute TTL
(one-time use, deleted on successful verification). The response never
reveals whether an email is registered — same message either way.

- **Rate-limited**: after 5 incorrect codes for an email, further attempts
  are blocked for 15 minutes (in-memory; see the note in `LoginAttemptService`
  about backing this with Redis for multi-instance deployments).
- **No SMTP configured?** The OTP is logged to the backend console instead
  of failing, so the whole flow is testable locally without a real mail
  account. Configure `MAIL_USERNAME`/`MAIL_PASSWORD` in `.env` to send real
  emails (e.g. a Gmail app password).

---

## Notifications

In-app notifications are created automatically on every session/review
lifecycle event — no polling of external services, no separate job:

| Event | Recipient | 
|---|---|
| Someone requests a session | The mentor |
| Mentor accepts | The requester |
| Mentor declines | The requester |
| Mentor marks complete | The requester (prompted to review) |
| A review is submitted | The mentor |

The bell icon in the app shell polls `GET /api/notifications/unread-count`
every 30 seconds and lazily loads the full list only when opened.

---

## Admin Panel & Audit Logging

Available at `/admin` in the frontend for users with the `ADMIN` role
(enforced server-side by `SecurityConfig`'s `/api/admin/**` →
`hasRole('ADMIN')` rule, not just hidden in the UI). Covers:

- **Platform stats** — total users by role, session counts by status,
  review count and average rating.
- **User management** — enable/disable any account (an admin can't disable
  their own).
- **Audit log** — every admin action (currently: enabling/disabling users)
  is recorded immutably with the actor, action, target, and timestamp, and
  viewable in the Audit Log tab.

To test this locally, register a user then update their role directly in
the database (or seed one — see `seed_data.sql`, which includes an
`admin@skillswap.dev` account).

---

## Learning Dashboard

`GET /api/dashboard/stats` aggregates, for the current user: skills
offering/learning counts, completed sessions (as learner and as mentor),
pending requests in both directions, average rating received, and a
6-month completed-session activity series — rendered as an area chart on
the frontend dashboard (via `recharts`) alongside stat cards.

---

## Sample / Seed Data

Sample users for local testing (password for all: `Password1!`):

| Email | Role |
|---|---|
| admin@skillswap.dev | ADMIN |
| ananya.mentor@skillswap.dev | MENTOR |
| rahul.student@skillswap.dev | STUDENT |

Load them with:
```bash
psql -U skillswap -d skillswap -f backend/src/main/resources/db/seed/seed_data.sql
```

---

## Security Notes

- Passwords hashed with BCrypt (strength 12).
- Stateless JWT auth; access tokens short-lived (15 min default), refresh
  tokens longer-lived (7 days) and rotated on use.
- In-memory brute-force lockout after 5 failed logins (15 min cooldown) —
  see `LoginAttemptService`. For a multi-instance production deployment,
  swap this for a Redis-backed counter (the interface stays the same).
- Centralized exception handling — no stack traces or internal details are
  ever returned to clients.
- CORS origins are explicit and environment-driven, never wildcarded.
- All secrets (JWT key, DB credentials, mail credentials) come from
  environment variables — nothing sensitive is hardcoded.

---

## Deployment

### Backend → Render
1. New Web Service → connect this repo, root directory `backend`.
2. Build command: uses the provided `Dockerfile` automatically (Render
   supports Docker-based services natively).
3. Set the same environment variables as `.env.example` in Render's
   dashboard, pointing `DB_URL` at your Neon/Supabase Postgres instance.

### Frontend → Vercel
1. Import the repo, set root directory to `frontend`.
2. Framework preset: Vite.
3. Set `VITE_API_BASE_URL` to your deployed Render backend URL (e.g.
   `https://skillswap-backend.onrender.com/api`).

### Database → Neon or Supabase
Create a Postgres instance, copy the connection string into `DB_URL` /
`DB_USERNAME` / `DB_PASSWORD` on Render. Flyway will run migrations
automatically on backend startup.

---

## Roadmap

- [x] Auth: register / login / refresh / JWT, brute-force protection,
      forgot-password OTP (Redis-backed, rate-limited)
- [x] Docker Compose local stack, Flyway schema, seed data
- [x] Profile management: bio/links/location/availability, skills
      (offering/learning + proficiency), resume upload (local/Cloudinary)
- [x] Skill exchange: mentor search/filter, request/accept/reject/complete,
      reviews (1–5 rating, one per session), bookmarks
- [x] AI features via Ollama: roadmap generator, skill-gap analysis,
      project suggestions, resume review, interview tips, weekly planner,
      mentor recommendation + smart matching, dashboard summary, chat assistant
- [x] Notifications on every session/review lifecycle event
- [x] Learning Dashboard: aggregated stats + 6-month activity chart
- [x] Admin panel: user management, platform stats, immutable audit log
- [x] Premium UI pass: sidebar app shell, dark mode, Sora/Inter type pairing,
      brand mark, consistent glass-card design system, recharts data viz

### What's genuinely still open
- [ ] Auto-extract resume text from the uploaded file for AI review
      (currently takes pasted text — see AI Features section above)
- [ ] Real-time notifications (currently 30s polling, not WebSockets/SSE)
- [ ] CSRF token handling for browser-based clients beyond the SPA's own
      JWT flow (not applicable to this stateless-JWT setup, but worth
      revisiting if cookie-based auth is ever added)
- [ ] I attempted to run this end-to-end via Docker in the sandbox this was
      built in, and here's exactly what I found:
      - **Docker itself works** — `dockerd` starts and runs fine.
      - **Docker Hub is blocked**: `docker pull hello-world` →
        `403 Forbidden` from `registry-1.docker.io`. No image can be pulled
        (postgres, redis, nginx, node, eclipse-temurin — none of them),
        so `docker compose up` cannot complete in this sandbox regardless
        of how the compose file is written.
      - **Maven Central is blocked** the same way: `mvn dependency:resolve`
        → `403 Forbidden` from `repo.maven.apache.org`. So the backend
        can't be compiled here either.
      - **What I could verify instead**: installed PostgreSQL 16 and Redis
        directly via `apt` (which *is* reachable) and ran all 5 Flyway
        migrations plus the seed data against a real Postgres instance —
        all applied cleanly, foreign keys/check constraints/unique
        constraints included. Verified Redis `SET ... EX` / `GET` / `DEL`
        semantics match exactly what `OtpService` relies on. Rebuilt the
        frontend from a clean `npm install` and served the actual
        production bundle — HTML and JS both return `200`.
      - **What that leaves unverified**: an actual `mvn clean package` /
        `docker compose up` run, purely because this sandbox's network
        policy blocks the two registries needed. On a normal machine
        without that restriction, this should just work — but "should"
        isn't "does," so this is the first thing worth trying on your end,
        and I'd genuinely rather hear about a real error than have you
        assume silence means success.

### Skill Exchange — how it works

1. A learner searches mentors by skill (`GET /api/mentors/search?skill=...`).
2. They send a session request (`POST /api/sessions`) naming the mentor and
   skill — this is blocked if the mentor doesn't actually offer that skill,
   or if a pending request for the same pair already exists.
3. The mentor accepts or rejects it from their incoming queue.
4. Once accepted, the mentor marks it completed when the session has happened.
5. Only the original requester can leave a review, only after completion,
   and only once per session.
