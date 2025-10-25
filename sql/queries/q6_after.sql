-- Recreate the index
CREATE INDEX IF NOT EXISTS idx_tasks_user_id ON tasks(user_id);

-- Query plan AFTER the index
EXPLAIN (ANALYZE, BUFFERS)
SELECT *
FROM tasks
WHERE user_id = 1;