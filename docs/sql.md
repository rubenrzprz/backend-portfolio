# SQL — Local PostgreSQL (Docker-only, no native client)

This guide shows how to run PostgreSQL in Docker, apply schema/seed scripts, execute queries, and capture results — **using only the Postgres container** (no native `psql` install required). All commands reference the root **`.env`** file.

---

## Prerequisites

* **Docker Desktop** (includes Docker Engine + Compose v2)
* A root **`.env`** file (create from the template below)

**Create `.env` (once):**

```bash
cp .env.example .env
# Edit values if needed. Defaults for local dev are safe.
```

**`.env` keys used:**

```
POSTGRES_USER=bp_user
POSTGRES_PASSWORD=bp_pass
POSTGRES_DB=bp_db
POSTGRES_PORT=5432
```

> Keep `.env` out of Git. Only `.env.example` is committed.

---

## Start / Stop the database

> Run these from the **repo root** so the relative compose path works.

**Start (detached):**

```bash
docker compose -f sql/docker-compose.yml --env-file ./.env up -d
```

**Check status:**

```bash
docker compose -f sql/docker-compose.yml --env-file ./.env ps
```

**Stop (keep data volume):**

```bash
docker compose -f sql/docker-compose.yml --env-file ./.env down
```

**Stop + remove volume (destructive reset):**

```bash
docker compose -f sql/docker-compose.yml --env-file ./.env down -v
```

> The compose file uses a named volume to persist data between restarts.

---

## Connect with `psql` **inside** the container

You don’t need a local client. Open a shell **inside** the running Postgres container and run `psql` there.

```bash
# open a bash shell in the "db" service
docker compose -f sql/docker-compose.yml --env-file ./.env exec db bash
```

Inside the container shell:

```bash
# connect to the DB using env variables provided by Compose
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"
-- You should see a prompt like:  bp_db=#
```

> If `env` isn’t populated in your shell for any reason, you can pass values explicitly:
> `psql -U bp_user -d bp_db`

---

## Apply schema and seed data

From the `psql` prompt (still inside the container):

```sql
\i sql/init/schema.sql
\i sql/init/seed.sql

-- quick checks
SELECT COUNT(*) AS users FROM users;
SELECT COUNT(*) AS tasks FROM tasks;
```

---

## Run queries and capture outputs

Use `\o` to write results to files under `sql/results/`, then run each `qN.sql`.

**Example (Q1):**

```sql
\o sql/results/q1.txt
\i sql/queries/q1.sql
\o
```

Repeat for `q2.sql` … `q8.sql`.

### EXPLAIN before/after (index demo)

```sql
-- BEFORE
\o sql/results/q6_before.txt
\i sql/queries/q6_before.sql
\o

-- Create index (safe if exists)
CREATE INDEX IF NOT EXISTS idx_tasks_user_id ON tasks(user_id);

-- AFTER
\o sql/results/q6_after.txt
\i sql/queries/q6_after.sql
\o
```

> Tip: If you want real timings and buffer usage, use:
> `EXPLAIN (ANALYZE, BUFFERS)` in the `q6_*` scripts.

---

## Folder layout (conventions)

```
sql/
├─ docker-compose.yml
├─ init/
│  ├─ schema.sql
│  └─ seed.sql
├─ queries/
│  ├─ q1.sql … q8.sql
│  ├─ q6_before.sql
│  └─ q6_after.sql
└─ results/
   ├─ q1.txt … q8.txt
   ├─ q6_before.txt
   └─ q6_after.txt
```

---

## Troubleshooting

* **Compose warns “variable not set”**
  Always pass the env file on every Compose command:

  ```bash
  docker compose -f sql/docker-compose.yml --env-file ./.env <cmd>
  ```

* **Container runs, but can’t connect with `psql`**
  Ensure the service is up:

  ```bash
  docker compose -f sql/docker-compose.yml --env-file ./.env ps
  ```

  Then exec into it:

  ```bash
  docker compose -f sql/docker-compose.yml --env-file ./.env exec db bash
  psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"
  ```

* **Port 5432 in use**
  Change `POSTGRES_PORT` in `.env` (e.g., `5433`) and restart:

  ```bash
  docker compose -f sql/docker-compose.yml --env-file ./.env down
  docker compose -f sql/docker-compose.yml --env-file ./.env up -d
  ```

* **EXPLAIN doesn’t show index usage**
  Verify index exists and your predicate/join uses the indexed column:

  ```sql
  \d tasks
  -- re-run EXPLAIN after CREATE INDEX
  ```

---

## Clean up

```bash
# stop containers (keep data)
docker compose -f sql/docker-compose.yml --env-file ./.env down

# remove containers + volume (fresh start)
docker compose -f sql/docker-compose.yml --env-file ./.env down -v
```

---

## Good practices

* Never commit real credentials. Keep `.env` local; commit `.env.example` only.
* Store all outputs in `sql/results/` for reproducibility and review.
