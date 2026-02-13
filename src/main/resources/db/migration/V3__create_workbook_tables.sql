CREATE TABLE workbooks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    description TEXT NOT NULL,
    difficulty BIGINT NOT NULL DEFAULT 3,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME,
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE workbook_problems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workbook_id BIGINT NOT NULL,
    problem_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME,
    UNIQUE KEY uk_workbook_problem (workbook_id, problem_id),
    INDEX idx_workbook_id (workbook_id),
    INDEX idx_problem_id (problem_id),
    CONSTRAINT fk_workbook_problems_workbook FOREIGN KEY (workbook_id) REFERENCES workbooks(id),
    CONSTRAINT fk_workbook_problems_problem FOREIGN KEY (problem_id) REFERENCES problems(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
