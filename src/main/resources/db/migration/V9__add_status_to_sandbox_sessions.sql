ALTER TABLE sandbox_sessions
    ADD COLUMN status VARCHAR(10) NOT NULL DEFAULT 'ACTIVE'
        COMMENT 'ACTIVE | CLOSED | EXPIRED'
        AFTER expires_at,
    ADD INDEX idx_status (status);
