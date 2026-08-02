<div align="center">

#  SkillSwap AI

### AI-Powered Peer Learning & Skill Exchange Platform

**Connect • Learn • Teach • Grow**

A modern full-stack platform where **students, freshers, and professionals**
can exchange technical skills, find mentors, schedule learning sessions, and
receive personalized AI guidance — powered entirely by **free local LLMs using Ollama**.

---

![Java](https://img.shields.io/badge/Java-21-red?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-success?style=for-the-badge&logo=springboot)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-5-blue?style=for-the-badge&logo=typescript)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-7-red?style=for-the-badge&logo=redis)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)
![JWT](https://img.shields.io/badge/Auth-JWT-orange?style=for-the-badge)
![Ollama](https://img.shields.io/badge/AI-Ollama-black?style=for-the-badge)

![License](https://img.shields.io/badge/License-MIT-success?style=for-the-badge)
![Made With Love](https://img.shields.io/badge/Made%20With-❤️-ff69b4?style=for-the-badge)

</div>

---

# 📖 Overview

SkillSwap AI is an **AI-powered peer learning ecosystem** designed to help
students and professionals learn faster by exchanging knowledge directly with
other learners.

Instead of relying solely on expensive online courses, SkillSwap enables users
to:

- 👨‍🏫 Find mentors based on real technical skills
- 🤝 Exchange knowledge with peers
- 📅 Schedule learning sessions
- ⭐ Build reputation through reviews
- 📊 Track learning progress
- 🤖 Receive AI-powered career guidance
- 📄 Improve resumes
- 🎯 Generate personalized learning roadmaps
- 💬 Chat with an AI learning assistant

Unlike many AI platforms, **SkillSwap AI requires no paid AI APIs**.

Every AI capability runs completely offline using **Ollama**, making the
platform:

- Free
- Privacy-friendly
- API-key free
- Fully self-hosted

---

# ✨ Key Highlights

## 🎓 Peer-to-Peer Learning

- Mentor discovery
- Smart skill matching
- Session booking
- Learning requests
- Session tracking
- Reviews & ratings
- Bookmarks

---

## 🤖 AI Career Assistant

Powered locally using Ollama.

Includes:

- Learning Roadmap Generator
- Skill Gap Analysis
- Resume Review
- Interview Preparation
- Project Suggestions
- Weekly Study Planner
- Mentor Recommendation
- Personalized Dashboard Summary
- AI Chat Assistant

---

## 🔒 Secure Authentication

- JWT Authentication
- Access & Refresh Tokens
- BCrypt Password Hashing
- OTP Password Reset
- Redis OTP Storage
- Brute Force Protection
- Role-Based Authorization

---

## 📊 Analytics Dashboard

Track your growth with

- Learning statistics
- Teaching statistics
- Session history
- Monthly activity
- Rating analytics
- Skill progress

---

## ⚙️ Admin Panel

Includes

- User Management
- Platform Statistics
- Audit Logs
- User Status Control
- Secure Role-Based Access

---

# 🌟 Why SkillSwap AI?

Most learning platforms focus only on **content consumption**.

SkillSwap focuses on **collaborative learning**.

Instead of asking:

> "Which course should I buy?"

SkillSwap asks:

> "Who can teach me this skill today?"

The platform combines:

- Human mentorship
- AI guidance
- Community learning
- Career development

inside one ecosystem.

---

# 🏗️ System Architecture

```text
                    +----------------------+
                    |      React App       |
                    |   (Vite + TS + UI)   |
                    +----------+-----------+
                               |
                         REST API (JWT)
                               |
+------------------------------------------------------------+
|                 Spring Boot Backend                        |
|------------------------------------------------------------|
| Authentication • Profiles • Mentors • Sessions             |
| AI Services • Dashboard • Notifications • Admin            |
+-----------+---------------+----------------+---------------+
            |               |                |
            |               |                |
     PostgreSQL         Redis            Ollama
   (Persistent DB)    OTP Cache      Local AI Models
```

---

# 🖥️ Technology Stack

| Layer | Technologies |
|--------|--------------|
| **Frontend** | React 19, TypeScript, Vite, Tailwind CSS v4, React Router, Axios, React Query, Framer Motion |
| **Backend** | Java 21, Spring Boot 3, Spring Security, Spring Data JPA |
| **Authentication** | JWT Access Token, Refresh Token |
| **Database** | PostgreSQL |
| **Caching** | Redis |
| **AI Engine** | Ollama (Qwen2.5, Llama 3.2, Phi-3 Mini, DeepSeek-R1) |
| **ORM** | Hibernate / Spring Data JPA |
| **Migration** | Flyway |
| **Documentation** | Swagger OpenAPI |
| **Storage** | Local Storage / Cloudinary |
| **Deployment** | Docker Compose |
| **Hosting** | Render + Vercel + Neon/Supabase |

---

# 🎯 Core Features

## 👤 User Features

- User Registration
- Secure Login
- JWT Authentication
- Password Reset
- Profile Management
- Resume Upload
- Skill Management
- Mentor Search
- Session Booking
- Notifications
- Dashboard
- Reviews
- Bookmarks

---

## 🤖 AI Features

| Feature | Status |
|----------|--------|
| Learning Roadmap | ✅ |
| Resume Review | ✅ |
| Skill Gap Analysis | ✅ |
| AI Chat Assistant | ✅ |
| Interview Tips | ✅ |
| Weekly Study Planner | ✅ |
| Mentor Recommendation | ✅ |
| Dashboard Summary | ✅ |
| Project Suggestions | ✅ |

---

## 🛡 Security Features

- BCrypt Password Encryption
- JWT Authentication
- Refresh Tokens
- Redis OTP Verification
- Brute Force Protection
- Role-Based Authorization
- Environment Variable Secrets
- Centralized Exception Handling

---

# 📸 Application Preview

> Replace the placeholders below with your project screenshots before publishing.

## Landing Page

```
docs/screenshots/landing-page.png
```

---

## Dashboard

```
docs/screenshots/dashboard.png
```

---

## AI Resume Review

```
docs/screenshots/resume-review.png
```

---

## Mentor Search

```
docs/screenshots/mentor-search.png
```

---

## Admin Panel

```
docs/screenshots/admin-panel.png
```

---

# ⭐ Project Goals

SkillSwap AI was built to demonstrate production-level software engineering concepts including:

- Clean Architecture
- RESTful API Design
- JWT Security
- Spring Boot Best Practices
- Database Design
- Docker Deployment
- AI Integration
- Modern React Development
- Scalable Backend Design
- Full-Stack System Integration

This project is intended as a **portfolio-quality full-stack application**
showcasing modern software engineering practices alongside practical AI integration using local language models.
---

# 📂 Project Structure

```text
skillswap-ai/
│
├── backend/
│   ├── src/main/java/com/skillswap/
│   │   ├── config/                 # Security, CORS, Swagger configuration
│   │   ├── controller/             # REST API Controllers
│   │   ├── dto/                    # Request & Response DTOs
│   │   ├── entity/                 # JPA Entities
│   │   ├── exception/              # Global Exception Handling
│   │   ├── repository/             # Spring Data JPA Repositories
│   │   ├── security/               # JWT Authentication & Filters
│   │   ├── service/                # Business Logic
│   │   │   └── ai/                 # Ollama AI Integration
│   │   ├── util/
│   │   └── SkillSwapApplication.java
│   │
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── db/
│   │   │   ├── migration/          # Flyway Migrations
│   │   │   └── seed/               # Seed Data
│   │   └── static/
│   │
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── assets/
│   │   ├── components/
│   │   ├── context/
│   │   ├── hooks/
│   │   ├── layouts/
│   │   ├── pages/
│   │   ├── routes/
│   │   ├── services/
│   │   ├── types/
│   │   └── utils/
│   │
│   ├── public/
│   ├── Dockerfile
│   └── package.json
│
├── postman/
│
├── docker-compose.yml
├── .env.example
├── README.md
└── LICENSE
```

---

# ⚡ Quick Start

SkillSwap AI can be started in **under 5 minutes**.

### Prerequisites

Install the following before running the project.

| Software | Version |
|-----------|----------|
| Java | 21+ |
| Maven | 3.9+ |
| Node.js | 20+ |
| Docker Desktop | Latest |
| PostgreSQL | 16+ |
| Redis | 7+ |
| Ollama | Latest |

---

# 📥 Clone Repository

```bash
git clone https://github.com/yourusername/skillswap-ai.git

cd skillswap-ai
```

---

# ⚙️ Environment Configuration

Copy the example environment file.

```bash
cp .env.example .env
```

Open `.env`

Configure your environment variables.

```properties
############################
# DATABASE
############################

DB_NAME=skillswap
DB_USERNAME=skillswap
DB_PASSWORD=change-me

############################
# JWT
############################

JWT_SECRET=replace-with-strong-secret
JWT_ACCESS_EXPIRY_MS=900000
JWT_REFRESH_EXPIRY_MS=604800000

############################
# CORS
############################

CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173

############################
# AI
############################

OLLAMA_BASE_URL=http://host.docker.internal:11434
OLLAMA_MODEL=qwen2.5:7b

############################
# MAIL
############################

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=
MAIL_PASSWORD=
```

> **Important:** Never commit `.env` to GitHub.

---

# 🐳 Running with Docker (Recommended)

Docker automatically starts

- PostgreSQL
- Redis
- Spring Boot Backend
- React Frontend

### Build everything

```bash
docker compose up --build
```

Docker will automatically

- Create PostgreSQL database
- Run Flyway migrations
- Start Redis
- Start Backend
- Start Frontend

---

### Access the application

| Service | URL |
|----------|-----|
| Frontend | http://localhost:3000 |
| Backend | http://localhost:8080 |
| Swagger | http://localhost:8080/swagger-ui/index.html |
| OpenAPI | http://localhost:8080/v3/api-docs |

---

### Stop Containers

```bash
docker compose down
```

---

### Rebuild Containers

```bash
docker compose down

docker compose up --build
```

---

# 💻 Running Without Docker

## 1. Start PostgreSQL

Create a database.

```sql
CREATE DATABASE skillswap;
```

---

## 2. Start Redis

Linux

```bash
redis-server
```

Windows

Launch Redis or Docker Redis.

---

## 3. Start Backend

```bash
cd backend

mvn clean install

mvn spring-boot:run
```

Backend starts on

```
http://localhost:8080
```

---

## 4. Start Frontend

```bash
cd frontend

npm install

npm run dev
```

Frontend starts on

```
http://localhost:5173
```

---

# 🤖 AI Setup (Ollama)

SkillSwap AI uses **local LLMs** through Ollama.

No OpenAI API.

No Gemini API.

No Anthropic API.

Everything runs on your own machine.

---

## Install Ollama

Download from

https://ollama.com/download

---

## Pull a Model

Recommended

```bash
ollama pull qwen2.5:7b
```

Other supported models

```bash
ollama pull llama3.2

ollama pull phi3:mini

ollama pull deepseek-r1:7b
```

---

## Start Ollama

```bash
ollama serve
```

Verify

```bash
ollama list
```

Example

```
NAME

qwen2.5:7b
```

---

## Test Ollama

```bash
curl http://localhost:11434/api/tags
```

---

# 🔄 Switching AI Models

Changing models requires only one configuration change.

Example

```properties
OLLAMA_MODEL=qwen2.5:7b
```

or

```properties
OLLAMA_MODEL=llama3.2
```

or

```properties
OLLAMA_MODEL=deepseek-r1:7b
```

No backend code changes are required.

---

# 🗄 Database Migrations

SkillSwap AI uses **Flyway** for schema versioning.

Every backend startup automatically

- Creates tables
- Updates schema
- Applies migrations safely

Migration files

```
backend/src/main/resources/db/migration
```

Example

```
V1__create_users.sql

V2__create_profiles.sql

V3__create_sessions.sql

V4__notifications.sql

V5__audit_logs.sql
```

---

# 🌱 Seed Sample Data

Load sample users.

```bash
psql -U skillswap \
-d skillswap \
-f backend/src/main/resources/db/seed/seed_data.sql
```

Default password

```
Password1!
```

---

# 📖 API Documentation

Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON

```
http://localhost:8080/v3/api-docs
```

Import the included Postman collection from

```
postman/
```

to test every endpoint quickly.

---

# 🌐 Deployment

The project is designed for production deployment using modern cloud services.

| Component | Recommended Platform |
|------------|----------------------|
| Frontend | Vercel |
| Backend | Render |
| PostgreSQL | Neon / Supabase |
| File Storage | Cloudinary |
| AI | Ollama |
| Containerization | Docker |

---

# 🔧 Environment Variables

| Variable | Description |
|-----------|-------------|
| DB_URL | PostgreSQL connection URL |
| DB_USERNAME | Database username |
| DB_PASSWORD | Database password |
| JWT_SECRET | Secret key for JWT signing |
| JWT_ACCESS_EXPIRY_MS | Access token expiration |
| JWT_REFRESH_EXPIRY_MS | Refresh token expiration |
| CORS_ALLOWED_ORIGINS | Allowed frontend origins |
| OLLAMA_BASE_URL | Ollama server URL |
| OLLAMA_MODEL | AI model name |
| MAIL_HOST | SMTP Host |
| MAIL_PORT | SMTP Port |
| MAIL_USERNAME | SMTP Username |
| MAIL_PASSWORD | SMTP Password |

---

# ✅ Verify Installation

After successful setup, you should be able to access:

| Component | Status |
|------------|--------|
| Frontend | ✅ Running |
| Backend | ✅ Running |
| PostgreSQL | ✅ Connected |
| Redis | ✅ Connected |
| Swagger | ✅ Available |
| Flyway | ✅ Migrations Applied |
| Ollama | ✅ AI Ready |

If all the above services are running successfully, your SkillSwap AI development environment is fully configured and ready for development.
---

# 🔌 REST API Reference

SkillSwap AI follows RESTful architecture principles with JWT-based authentication.

All endpoints are versioned under:

```
/api
```

Interactive API documentation is available through Swagger.

| Resource | URL |
|----------|-----|
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI Specification | http://localhost:8080/v3/api-docs |

---

# 🔐 Authentication APIs

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/register` | POST | Register a new account |
| `/api/auth/login` | POST | Login user |
| `/api/auth/refresh` | POST | Refresh JWT token |
| `/api/auth/forgot-password` | POST | Generate OTP |
| `/api/auth/reset-password` | POST | Reset password |

---

# 👤 Profile APIs

| Endpoint | Method |
|----------|--------|
| `/api/profile/me` | GET |
| `/api/profile/me` | PUT |
| `/api/profile/{userId}` | GET |
| `/api/profile/me/resume` | POST |
| `/api/profile/me/skills` | POST |
| `/api/profile/me/skills` | DELETE |
| `/api/profile/skills/search` | GET |

---

# 👨‍🏫 Mentor APIs

| Endpoint | Method |
|----------|--------|
| `/api/mentors/search` | GET |
| `/api/mentors/{mentorId}/reviews` | GET |

---

# 📅 Session APIs

| Endpoint | Method |
|----------|--------|
| `/api/sessions` | POST |
| `/api/sessions/incoming` | GET |
| `/api/sessions/outgoing` | GET |
| `/api/sessions/{id}/accept` | POST |
| `/api/sessions/{id}/reject` | POST |
| `/api/sessions/{id}/complete` | POST |
| `/api/sessions/{id}/cancel` | POST |
| `/api/sessions/{id}/review` | POST |

---

# 🔖 Bookmark APIs

| Endpoint | Method |
|----------|--------|
| `/api/bookmarks` | GET |
| `/api/bookmarks` | POST |
| `/api/bookmarks` | DELETE |

---

# 🤖 AI APIs

| Endpoint | Purpose |
|----------|----------|
| `/api/ai/roadmap` | Personalized Learning Roadmap |
| `/api/ai/skill-gap` | Skill Gap Analysis |
| `/api/ai/project-ideas` | AI Project Suggestions |
| `/api/ai/resume-review` | Resume Review |
| `/api/ai/interview-tips` | Interview Preparation |
| `/api/ai/study-plan` | Weekly Study Planner |
| `/api/ai/mentor-recommendation` | Smart Mentor Recommendation |
| `/api/ai/dashboard-summary` | Dashboard Insights |
| `/api/ai/chat` | AI Chat Assistant |

---

# 🔔 Notification APIs

| Endpoint | Method |
|----------|--------|
| `/api/notifications` | GET |
| `/api/notifications/unread-count` | GET |
| `/api/notifications/{id}/read` | POST |
| `/api/notifications/read-all` | POST |

---

# 📊 Dashboard APIs

| Endpoint | Method |
|----------|--------|
| `/api/dashboard/stats` | GET |

---

# 👑 Admin APIs

| Endpoint | Method |
|----------|--------|
| `/api/admin/users` | GET |
| `/api/admin/users/{id}/status` | PATCH |
| `/api/admin/stats` | GET |
| `/api/admin/audit-logs` | GET |

---

# 🤖 AI Features

SkillSwap AI integrates **Local Large Language Models (LLMs)** through **Ollama**, allowing users to access intelligent career guidance while keeping all processing local and private.

Unlike cloud-based AI services, no API keys or third-party AI providers are required.

---

## 🚀 Learning Roadmap Generator

Generates a personalized roadmap based on:

- Existing skills
- Learning goals
- Current proficiency
- Career aspirations

Example Output

- Beginner Roadmap
- Intermediate Milestones
- Recommended Projects
- Learning Resources

---

## 📈 Skill Gap Analysis

Compares your profile against a target role.

Provides

- Missing Skills
- Important Technologies
- Suggested Learning Order
- Priority Ranking

---

## 💡 AI Project Suggestions

Generates project ideas based on

- Technology
- Experience Level
- Career Goal

Each suggestion includes

- Description
- Features
- Difficulty
- Learning Outcome

---

## 📄 Resume Review

The AI reviews pasted resume content and provides

- ATS Improvements
- Missing Sections
- Better Bullet Points
- Grammar Suggestions
- Technical Recommendations

---

## 🎯 Interview Preparation

Provides

- Common Questions
- Technical Concepts
- HR Questions
- Coding Topics
- Preparation Strategy

---

## 📅 Weekly Study Planner

Creates a study schedule based on

- Available Hours
- Target Skill
- Current Knowledge
- Learning Timeline

---

## 👨‍🏫 Mentor Recommendation

Combines database matching with AI reasoning.

Recommendations consider

- Shared Skills
- Ratings
- Experience
- Availability
- Session History

---

## 📊 Dashboard Summary

Analyzes user activity to generate

- Learning Progress
- Session Insights
- Productivity Summary
- Personalized Recommendations

---

## 💬 AI Chat Assistant

A conversational assistant capable of

- Answering technical questions
- Explaining concepts
- Suggesting learning paths
- Providing career guidance
- Helping with projects

Conversation history is maintained on the client for a smooth chat experience.

---

# 📚 Skill Exchange Workflow

The complete mentor-learning cycle consists of the following stages.

```text
Search Mentor
      │
      ▼
Send Learning Request
      │
      ▼
Mentor Accepts / Rejects
      │
      ▼
Learning Session
      │
      ▼
Session Completed
      │
      ▼
Review & Rating
      │
      ▼
Dashboard Updated
```

---

# 🔔 Notification System

Users receive real-time platform notifications for important events.

Supported events include

- New learning requests
- Accepted sessions
- Rejected sessions
- Completed sessions
- Review reminders
- New ratings received

Unread notification counts are automatically refreshed by the frontend.

---

# 📊 Learning Dashboard

The dashboard provides meaningful insights into each user's learning journey.

### Metrics

- Skills Offered
- Skills Learning
- Completed Sessions
- Pending Requests
- Mentor Ratings
- Monthly Activity
- Learning Progress

Charts are rendered using **Recharts** for a responsive and interactive experience.

---

# 👑 Admin Dashboard

Administrators have access to advanced management tools.

### Platform Statistics

- Total Users
- Students
- Mentors
- Admins
- Completed Sessions
- Active Sessions
- Reviews
- Average Rating

---

### User Management

Administrators can

- Enable Users
- Disable Users
- Search Accounts
- View User Details

Self-disabling is intentionally prevented.

---

### Audit Logging

Every administrative action is permanently recorded.

Each log contains

- Administrator
- Action
- Target User
- Timestamp
- Details

This provides accountability and traceability for sensitive operations.

---

# 🔒 Security Features

Security is implemented throughout the application following modern backend best practices.

### Authentication

- JWT Access Tokens
- Refresh Tokens
- Stateless Authentication

---

### Password Security

- BCrypt Password Hashing
- Strength Factor 12
- Secure Password Storage

---

### Authorization

- Role-Based Access Control
- Student
- Mentor
- Administrator

---

### OTP Verification

Forgot Password functionality uses

- 6-Digit OTP
- Redis Storage
- 10-Minute Expiration
- One-Time Usage

---

### Login Protection

Brute-force attacks are mitigated using

- Failed Login Counter
- Temporary Account Lock
- Rate Limiting

---

### Backend Protection

- Global Exception Handling
- Input Validation
- Environment Variables
- Secure CORS Configuration
- No Sensitive Data Exposure
- Protected Admin APIs

---

# 📁 File Storage

SkillSwap AI supports two interchangeable storage providers.

### Local Storage

Suitable for development.

Files are stored in

```
uploads/
```

---

### Cloudinary

Recommended for production.

Benefits

- CDN Delivery
- Secure Storage
- High Availability
- Image Optimization

Switching providers requires only configuration changes without modifying application code.

---

# 🧪 Sample Accounts

For local development, the project includes seed users.

| Email | Role |
|--------|------|
| admin@skillswap.dev | ADMIN |
| ananya.mentor@skillswap.dev | MENTOR |
| rahul.student@skillswap.dev | STUDENT |

Default Password

```
Password1!
```

---

# 🏆 Design Principles

SkillSwap AI was built with the following engineering principles.

- Clean Architecture
- Separation of Concerns
- SOLID Principles
- RESTful Design
- Secure Authentication
- Scalable Backend
- Responsive Frontend
- Production-Ready Deployment
- AI-First User Experience
- Modular Codebase
- Docker-Based Development
- Maintainable Project Structure

The project aims to serve as a production-quality reference implementation for modern full-stack software engineering with practical local AI integration.
---

# 🚀 Deployment Guide

SkillSwap AI is designed with cloud-native deployment in mind. Every component can be deployed independently for scalability and easier maintenance.

## 🖥 Backend Deployment (Render)

### Step 1

Create a **New Web Service** on Render.

### Step 2

Connect your GitHub repository.

### Step 3

Select

```
Root Directory:
backend
```

### Step 4

Render automatically detects the included Dockerfile.

### Step 5

Configure the following environment variables.

```
DB_URL
DB_USERNAME
DB_PASSWORD

JWT_SECRET

JWT_ACCESS_EXPIRY_MS
JWT_REFRESH_EXPIRY_MS

CORS_ALLOWED_ORIGINS

OLLAMA_BASE_URL
OLLAMA_MODEL

MAIL_HOST
MAIL_PORT
MAIL_USERNAME
MAIL_PASSWORD

STORAGE_PROVIDER

CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
```

### Step 6

Deploy.

Flyway automatically executes all database migrations during application startup.

---

# 🌐 Frontend Deployment (Vercel)

### Step 1

Import the GitHub repository into Vercel.

### Step 2

Select

```
Root Directory

frontend
```

### Step 3

Framework Preset

```
Vite
```

### Step 4

Configure

```
VITE_API_BASE_URL=https://your-backend-url/api
```

### Step 5

Deploy.

---

# 🗄 Database Deployment

Recommended PostgreSQL providers

- Neon
- Supabase
- Railway PostgreSQL

Update

```
DB_URL
DB_USERNAME
DB_PASSWORD
```

to use your production database.

---

# ☁ Cloudinary Configuration

For production resume uploads

```
STORAGE_PROVIDER=cloudinary
```

Configure

```
CLOUDINARY_CLOUD_NAME

CLOUDINARY_API_KEY

CLOUDINARY_API_SECRET
```

No code changes are required.

---

# 📦 Docker Deployment

Start the complete application.

```bash
docker compose up --build
```

Stop

```bash
docker compose down
```

Rebuild

```bash
docker compose up --build --force-recreate
```

---

# ⚙ Production Checklist

Before deployment ensure

- Strong JWT Secret
- HTTPS Enabled
- Secure Database Credentials
- Cloudinary Configured
- SMTP Configured
- AI Model Installed
- Environment Variables Set
- Database Backups Enabled
- Logging Enabled
- Monitoring Enabled

---

# 🧪 Testing

## Backend

Run all unit tests.

```bash
cd backend

mvn test
```

---

## Frontend

```bash
cd frontend

npm run build
```

---

## API Testing

Swagger

```
http://localhost:8080/swagger-ui/index.html
```

Postman Collection

```
postman/
```

---

# 📈 Future Improvements

The current platform is feature-complete for its core vision. The following enhancements are planned for future releases.

## AI

- AI Voice Mentor
- AI Mock Interviews
- AI Code Review
- AI Pair Programming
- AI Coding Challenges
- AI Learning Analytics
- AI Skill Certification Suggestions
- AI Personalized Career Planning

---

## Platform

- Video Calling
- Screen Sharing
- Live Chat
- Calendar Integration
- Session Reminders
- Real-Time Notifications
- Mentor Verification
- Learning Certificates
- Gamification
- Achievement Badges
- XP & Leaderboards

---

## Technical

- WebSockets
- Redis Pub/Sub
- Elasticsearch
- Kubernetes
- CI/CD Pipelines
- Prometheus Monitoring
- Grafana Dashboards
- Rate Limiting
- API Versioning
- Microservice Migration

---

# 📌 Current Limitations

The following limitations are known and intentionally documented.

- Resume review currently accepts extracted text instead of automatically parsing uploaded PDF/DOCX files.
- Notifications use polling instead of WebSockets.
- Login protection is in-memory; Redis-backed distributed rate limiting would be preferable for multi-instance deployments.
- AI quality depends on the locally installed Ollama model.
- GPU acceleration is optional but recommended for larger language models.

---

# 🤝 Contributing

Contributions are welcome.

## Development Workflow

1. Fork the repository.

2. Create a feature branch.

```bash
git checkout -b feature/new-feature
```

3. Commit your changes.

```bash
git commit -m "Add new feature"
```

4. Push.

```bash
git push origin feature/new-feature
```

5. Open a Pull Request.

---

## Coding Standards

Backend

- Java 21
- Spring Boot Best Practices
- SOLID Principles
- Clean Architecture

Frontend

- React Functional Components
- TypeScript
- React Query
- Tailwind CSS
- Reusable Components

General

- Meaningful Commit Messages
- Modular Code
- Proper Documentation
- Consistent Naming Conventions

---

# 🏆 Project Highlights

✔ Full Stack Application

✔ AI Powered Platform

✔ Local LLM Integration

✔ JWT Authentication

✔ Role-Based Authorization

✔ Redis OTP Verification

✔ Docker Support

✔ PostgreSQL Database

✔ Flyway Database Versioning

✔ Resume Upload

✔ Mentor Matching

✔ Learning Dashboard

✔ Notifications

✔ Admin Panel

✔ Audit Logging

✔ Production Ready Architecture

---

# 📊 Project Statistics

| Category | Details |
|----------|----------|
| Architecture | Monolithic Modular Architecture |
| Backend | Spring Boot 3 |
| Frontend | React 19 |
| Database | PostgreSQL |
| Authentication | JWT |
| Cache | Redis |
| AI | Ollama |
| API Documentation | Swagger |
| ORM | Spring Data JPA |
| Database Migration | Flyway |
| Containerization | Docker |
| Deployment | Render + Vercel |
| Language | Java 21 + TypeScript |

---

# 🎓 Learning Outcomes

This project demonstrates practical experience with:

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- REST API Design
- PostgreSQL
- Redis
- Hibernate
- Flyway
- Docker
- React
- TypeScript
- Tailwind CSS
- React Query
- Role-Based Authorization
- Local AI Integration
- Production Deployment
- Secure Software Development

---

# 📄 License

This project is licensed under the **MIT License**.

See the **LICENSE** file for complete details.

---

# 🙏 Acknowledgements

Special thanks to the open-source community and the technologies that made this project possible.

- Spring Boot
- React
- PostgreSQL
- Redis
- Docker
- Flyway
- Ollama
- Qwen Team
- Meta Llama
- Microsoft Phi
- DeepSeek AI
- Tailwind CSS
- Vite
- Swagger OpenAPI

---

# ⭐ Support

If you found this project useful,

- ⭐ Star this repository
- 🍴 Fork the project
- 🐛 Report issues
- 💡 Suggest new features
- 🤝 Contribute improvements

Your support helps improve the project for the community.

---

<div align="center">

## 🚀 SkillSwap AI

### Learn Together • Teach Together • Grow Together

**Built with ❤️ using Java, Spring Boot, React, PostgreSQL, Redis, Docker & Local AI (Ollama).**

⭐ **If you like this project, please consider giving it a star!**

</div>
