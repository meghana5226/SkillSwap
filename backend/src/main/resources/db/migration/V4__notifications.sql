CREATE TABLE notifications (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type                VARCHAR(30) NOT NULL
                            CHECK (type IN ('SESSION_REQUESTED', 'SESSION_ACCEPTED', 'SESSION_REJECTED',
                                            'SESSION_COMPLETED', 'REVIEW_RECEIVED')),
    message             VARCHAR(500) NOT NULL,
    related_session_id  UUID,
    is_read             BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_notifications_user ON notifications (user_id, created_at DESC);
CREATE INDEX idx_notifications_unread ON notifications (user_id, is_read);
