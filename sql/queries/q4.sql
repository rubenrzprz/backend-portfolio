-- Tasks per user
SELECT u.id AS user_id,
    u.name AS user_name,
    COUNT(t.user_id) AS task_count
FROM users u
LEFT JOIN tasks t ON t.user_id = u.id
GROUP BY u.id, u.name
ORDER BY task_count DESC, u.id ASC;
