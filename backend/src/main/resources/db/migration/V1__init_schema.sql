-- SkillSwap AI — initial schema
-- Covers: users/profiles. Skill exchange, sessions, reviews etc. are added
-- in later migrations (V2__, V3__...) as those features are built.

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name         VARCHAR(100)  NOT NULL,
    email             VARCHAR(255)  NOT NULL UNIQUE,
    password          VARCHAR(255)  NOT NULL,
    role              VARCHAR(20)   NOT NULL DEFAULT 'STUDENT'
                          CHECK (role IN ('STUDENT', 'MENTOR', 'ADMIN')),
    email_verified    BOOLEAN       NOT NULL DEFAULT FALSE,
    enabled           BOOLEAN       NOT NULL DEFAULT TRUE,

    bio               VARCHAR(1000),
    experience_level  VARCHAR(50),
    github_url        VARCHAR(255),
    linkedin_url      VARCHAR(255),
    portfolio_url     VARCHAR(255),
    resume_url        VARCHAR(255),
    location          VARCHAR(150),
    available         BOOLEAN       NOT NULL DEFAULT TRUE,

    created_at        TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role ON users (role);
