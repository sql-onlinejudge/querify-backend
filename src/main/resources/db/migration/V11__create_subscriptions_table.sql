CREATE TABLE subscriptions (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    VARCHAR(36)  NOT NULL,
    status     VARCHAR(20)  NOT NULL,
    started_at DATETIME(6)  NOT NULL,
    expires_at DATETIME(6)  NOT NULL,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6),
    deleted_at DATETIME(6),
    INDEX idx_subscriptions_user_id (user_id)
);
