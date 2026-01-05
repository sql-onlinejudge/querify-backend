CREATE TABLE problems (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          title VARCHAR(100) NOT NULL,
                          description TEXT NOT NULL,
                          `schema` TEXT NOT NULL,
                          initial_data TEXT,
                          answer_query TEXT NOT NULL,
                          expected_result TEXT NOT NULL,
                          time_limit INT NOT NULL DEFAULT 5,
                          difficulty INT NOT NULL,
                          solved_count INT NOT NULL DEFAULT 0,
                          submission_count INT NOT NULL DEFAULT 0,
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME,
                          deleted_at DATETIME,
                          INDEX idx_difficulty (difficulty),
                          INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;