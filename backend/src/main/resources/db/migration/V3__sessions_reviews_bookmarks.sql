-- Skill exchange core: session requests (offer/request/accept/reject/complete),
-- reviews (rate a mentor after a completed session), and bookmarks (save a user).

CREATE TABLE session_requests (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    requester_id   UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    mentor_id      UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    skill_id       UUID NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                       CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'CANCELLED', 'COMPLETED')),
    message        VARCHAR(500),
    scheduled_at   TIMESTAMPTZ,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_session_requests_mentor ON session_requests (mentor_id);
CREATE INDEX idx_session_requests_requester ON session_requests (requester_id);
CREATE INDEX idx_session_requests_status ON session_requests (status);

CREATE TABLE reviews (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id    UUID NOT NULL UNIQUE REFERENCES session_requests(id) ON DELETE CASCADE,
    reviewer_id   UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    mentor_id     UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    rating        INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment       VARCHAR(1000),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_reviews_mentor ON reviews (mentor_id);

CREATE TABLE bookmarks (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    bookmarked_user_id  UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT uq_bookmark UNIQUE (user_id, bookmarked_user_id)
);

CREATE INDEX idx_bookmarks_user ON bookmarks (user_id);
