CREATE TABLE submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    problem_id BIGINT NOT NULL,
    user_id CHAR(36) NOT NULL,
    query TEXT NOT NULL,
    status ENUM('PENDING', 'RUNNING', 'COMPLETED') NOT NULL,
    verdict ENUM('ACCEPTED', 'WRONG_ANSWER', 'TIME_LIMIT_EXCEEDED', 'RUNTIME_ERROR'),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME,
    deleted_at DATETIME,
    INDEX idx_problem_id (problem_id),
    INDEX idx_user_id (user_id),
    INDEX idx_problem_user (problem_id, user_id),
    CONSTRAINT fk_submissions_problem FOREIGN KEY (problem_id) REFERENCES problems(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
