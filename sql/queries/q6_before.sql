-- Compare query plans before/after the index on tasks(user_id)

-- Ensure the index is not present
DROP INDEX IF EXISTS idx_tasks_user_id;

-- Query plan BEFORE the index (ANALYZE executes the query)
EXPLAIN (ANALYZE, BUFFERS)
SELECT *
FROM tasks
WHERE user_id = 1;