-- Users with more than 1 task
SELECT u.id AS user_id,
    u.name AS user_name
FROM users u
WHERE u.id IN (
    SELECT t.user_id
    FROM tasks t
    GROUP BY t.user_id
    HAVING COUNT(t.id) > 1
)
ORDER BY u.id;