-- Only users who have tasks (1:N)
SELECT u.name AS user_name,
    t.title AS task_title,
    t.status AS task_status,
    t.created_at AS task_created_at
FROM users u
INNER JOIN tasks t
ON u.id = t.user_id
ORDER BY u.id ASC;