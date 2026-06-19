# SkillSwap 

A full-stack skill exchange platform where users can teach what they know, learn from others, and collaborate through skill-sharing sessions.

SkillSwap connects learners and mentors by enabling users to offer skills, request skills, send exchange requests, and track their learning journey through a modern and intuitive platform.

---

##  Features

### Authentication & Security

* JWT Authentication
* Secure Login & Registration
* Protected Routes
* Role-Based Access Control
* Spring Security Integration

### User Management

* User Profiles
* Bio & Headline Management
* Profile Picture Support
* Skill Portfolio

### Skills Marketplace

* Add Offered Skills
* Add Wanted Skills
* Search Skills
* Filter Skills
* Discover Community Members

### Skill Exchange Requests

* Send Skill Exchange Requests
* Accept Requests
* Reject Requests
* Complete Learning Sessions
* Request Status Tracking

### Dashboard

* User Statistics
* Skill Analytics
* Request Metrics
* Activity Overview

### Modern UI

* Responsive Design
* Dark/Light Theme Ready
* Startup-Level User Experience
* Mobile Friendly Interface

---

## 🛠️ Tech Stack

### Frontend

* React.js
* Vite
* React Router DOM
* Axios
* Context API
* React Hook Form
* Framer Motion
* Recharts
* Advanced CSS

### Backend

* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate

### Database

* MySQL

### Tools

* Git
* GitHub
* Maven
* Postman
* Thunder Client

---

##  Project Structure

```text
SkillSwap
│
├── skillswap-frontend
│   ├── src
│   ├── public
│   └── package.json
│
├── skillswap
│   ├── src
│   ├── pom.xml
│   └── mvnw
│
└── README.md
```

---

##  API Endpoints

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
```

### User

```http
GET /api/users/profile
PUT /api/users/profile
```

### Skills

```http
GET /api/skills
POST /api/skills
DELETE /api/skills/{id}
GET /api/skills/search
```

### Requests

```http
GET /api/requests
POST /api/requests/{skillId}
PUT /api/requests/{id}/accept
PUT /api/requests/{id}/reject
PUT /api/requests/{id}/complete
```

### Dashboard

```http
GET /api/dashboard/stats
```

---

## ⚙️ Local Setup

### Clone Repository

```bash
git clone https://github.com/meghana5226/SkillSwap.git
cd SkillSwap
```

### Backend Setup

```bash
cd skillswap
```

Configure MySQL database in:

```properties
application.properties
```

Run:

```bash
./mvnw spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

---

### Frontend Setup

```bash
cd skillswap-frontend
npm install
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

---

## 📊 Future Enhancements

* AI Skill Recommendations
* Real-Time Chat
* Video Learning Sessions
* Notification Center
* Skill Matching Engine
* Gamification System
* Learning Progress Tracking
* Certificates & Achievements
* Agentic AI Mentor Assistant

---

##  Project Goal

SkillSwap aims to make learning collaborative by enabling people to exchange skills directly with one another instead of relying solely on traditional courses. Users can teach, learn, collaborate, and grow through community-driven knowledge sharing.

---

##  Author

Meghana

GitHub:
https://github.com/meghana5226

---

⭐ If you found this project useful, consider giving it a star.
