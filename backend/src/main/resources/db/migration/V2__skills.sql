-- Adds the skills taxonomy: a canonical `skills` list, and `user_skills`
-- linking users to skills either as OFFERING (can teach) or LEARNING (want to learn).

CREATE TABLE skills (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    category    VARCHAR(100)
);

CREATE INDEX idx_skills_name ON skills (name);

CREATE TABLE user_skills (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id       UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    skill_id      UUID NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    type          VARCHAR(20) NOT NULL CHECK (type IN ('OFFERING', 'LEARNING')),
    proficiency   VARCHAR(20) CHECK (proficiency IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT')),

    CONSTRAINT uq_user_skill_type UNIQUE (user_id, skill_id, type)
);

CREATE INDEX idx_user_skills_user_id ON user_skills (user_id);
CREATE INDEX idx_user_skills_skill_id_type ON user_skills (skill_id, type);
