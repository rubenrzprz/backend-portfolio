-- Recent-first task list with secondary order
SELECT id, user_id, title, status, created_at
FROM tasks
ORDER BY created_at DESC, id ASC;