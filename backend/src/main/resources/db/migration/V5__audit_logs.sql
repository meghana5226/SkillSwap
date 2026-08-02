CREATE TABLE audit_logs (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    actor_user_id  UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    action         VARCHAR(100) NOT NULL,
    target_type    VARCHAR(100),
    target_id      UUID,
    details        VARCHAR(1000),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_logs_created_at ON audit_logs (created_at DESC);
