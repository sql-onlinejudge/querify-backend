ALTER TABLE problems
    MODIFY updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    ADD INDEX idx_deleted_at (deleted_at);

ALTER TABLE test_cases
    MODIFY updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    ADD INDEX idx_deleted_at (deleted_at);

ALTER TABLE submissions
    MODIFY updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    DROP INDEX idx_problem_id,
    ADD INDEX idx_deleted_at (deleted_at);
