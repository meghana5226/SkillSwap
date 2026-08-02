-- Seed data for local development / demos.
-- Passwords below are all the plaintext value: Password1!
-- Hash generated with BCrypt strength 12 (matches SecurityConfig.passwordEncoder()).
--
-- Run manually against your local DB if you want sample data:
--   psql -U skillswap -d skillswap -f backend/src/main/resources/db/seed/seed_data.sql

INSERT INTO users (id, full_name, email, password, role, email_verified, enabled, bio, experience_level, github_url, available)
VALUES
    (gen_random_uuid(), 'Admin User', 'admin@skillswap.dev',
     '$2b$12$zGp6uIwB3rGhHzLiuWuYeuQJ9JdEYn8ldviMImbsre0.cTZ8REbxi',
     'ADMIN', true, true, 'Platform administrator.', 'Senior', 'https://github.com/skillswap', true),

    (gen_random_uuid(), 'Ananya Rao', 'ananya.mentor@skillswap.dev',
     '$2b$12$zGp6uIwB3rGhHzLiuWuYeuQJ9JdEYn8ldviMImbsre0.cTZ8REbxi',
     'MENTOR', true, true, 'Backend engineer, loves teaching Spring Boot and system design.', 'Senior',
     'https://github.com/ananya', true),

    (gen_random_uuid(), 'Rahul Mehta', 'rahul.student@skillswap.dev',
     '$2b$12$zGp6uIwB3rGhHzLiuWuYeuQJ9JdEYn8ldviMImbsre0.cTZ8REbxi',
     'STUDENT', true, true, 'Final year CS student learning full-stack development.', 'Beginner',
     'https://github.com/rahul', true);
