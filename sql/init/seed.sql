-- Minimal dataset for joins, grouping, and explain demos

-- Clean slate for repeatability (safe if empty)
TRUNCATE TABLE tasks RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Users
INSERT INTO users (name, email, created_at) VALUES
  ('Ruben',      'ruben@example.com',      NOW() - INTERVAL '10 days'),
  ('Elena',      'elena@example.com',      NOW() - INTERVAL '8 days'),
  ('Mateo',      'mateo@example.com',      NOW() - INTERVAL '6 days'),
  ('Lucia',      'lucia@example.com',      NOW() - INTERVAL '4 days');

-- Tasks
INSERT INTO tasks (user_id, title, status, created_at) VALUES
  -- Ruben (id=1): 3 tasks
  (1, 'Design API surface',     'todo',  NOW() - INTERVAL '5 days'),
  (1, 'Implement /items CRUD',  'doing', NOW() - INTERVAL '3 days'),
  (1, 'Write WebMvc tests',     'done',  NOW() - INTERVAL '2 days'),

  -- Elena (id=2): 1 task
  (2, 'Add error handling',     'doing', NOW() - INTERVAL '3 days'),

  -- Mateo (id=3): 2 tasks
  (3, 'Create DB schema',       'done',  NOW() - INTERVAL '2 days'),
  (3, 'Seed sample data',       'todo',  NOW() - INTERVAL '1 days');
