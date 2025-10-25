-- Users created in the last 7 days
SELECT id, name, email, created_at
FROM users
WHERE created_at >= NOW() - INTERVAL '7 days'
ORDER BY created_at DESC, id ASC;