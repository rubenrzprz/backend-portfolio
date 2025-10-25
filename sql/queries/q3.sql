-- Users with zero tasks
SELECT u.id, u.name, u.email, u.created_at
FROM users u
LEFT JOIN tasks t ON t.user_id = u.id
WHERE t.id IS NULL
ORDER BY u.id;