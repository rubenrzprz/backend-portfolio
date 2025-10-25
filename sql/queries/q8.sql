-- Case-insensitive search by task title
SELECT id, user_id, title, status, created_at
FROM tasks
WHERE title ILIKE '%data%'
ORDER BY id;