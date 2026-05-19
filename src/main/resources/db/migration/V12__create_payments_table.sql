CREATE TABLE payments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         VARCHAR(36)   NOT NULL,
    order_id        VARCHAR(64)   NOT NULL,
    payment_key     VARCHAR(200),
    amount          INT           NOT NULL,
    status          VARCHAR(20)   NOT NULL,
    subscription_id BIGINT,
    created_at      DATETIME(6)   NOT NULL,
    updated_at      DATETIME(6),
    UNIQUE INDEX uq_payments_order_id (order_id),
    INDEX idx_payments_user_id (user_id)
);
