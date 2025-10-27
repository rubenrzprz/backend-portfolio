# SQL & Local Database Guide

This document covers running **PostgreSQL** for the project using **Docker Compose** from the **repo root**, loading  schema/seed data, and running queries with reproducible outputs.

---

## Prerequisites

* Docker Desktop / Docker Engine
* Docker Compose (bundled with recent Docker versions)

> We run `psql` **inside** the container, so a local Postgres client is not necessary.

---

## Environment & `.env`

Because `docker-compose.yml` lives at the **repo root**, Docker Compose **auto-loads** the `.env` file in the **same
folder**. You can usually run commands **without** `--env-file`.

**Defaults (see `.env.example`):**

```
POSTGRES_USER=bp_user
POSTGRES_PASSWORD=bp_pass
POSTGRES_DB=bp_db
POSTGRES_PORT=5432
```

You can still be explicit if you want:

```bash
docker compose --env-file ./.env up -d
```

Or use an alternate env file (e.g., CI):

```bash
docker compose --env-file ./.env.ci up -d
```

---

## Repository Layout (SQL)

```
sql/
├─ init/          # schema.sql, seed.sql
├─ queries/       # q1.sql ... q8.sql (+ q6_before.sql / q6_after.sql)
└─ results/       # captured outputs via \o (created by you)
```

Compose mounts:

* `./sql` → `/sql` (read-only)
* `./sql/results` → `/sql/results` (read–write)
* `bp_pg_data` → `/var/lib/postgresql/data` → **Named Docker volume** that persists your database files

Anything you write to `/sql/results` **appears** in `sql/results/` in the repo.

---

## Run Postgres (Docker)

### Start containers

**Default (API + DB):**

```bash
docker compose up -d
```

**DB-only (preferred for SQL exercises):**

```bash
docker compose up -d db
```

See running containers:

```bash
docker compose ps
```

Stop everything (keeps data volume):

```bash
docker compose down
```

Reset everything (containers + named volume; **wipes DB data**):

```bash
docker compose down -v
```

---

## Connect with `psql` (inside the container)

We always open a shell **inside** the container first, then run `psql` so credentials come from the **container**
environment.

Open a shell:

```bash
docker compose exec db bash
```

Then run `psql`:

```bash
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"
-- You should see a prompt like:  bp_db=#
```

Exit `psql` with `\q`, then `exit` to leave the container shell.

---

## Load schema & seed

### Auto-init (first run only)

The Postgres container executes any `*.sql` under `/docker-entrypoint-initdb.d/` **only when the data volume is
new/empty**.

* `sql/init/schema.sql` → `/docker-entrypoint-initdb.d/01-schema.sql`
* `sql/init/seed.sql`   → `/docker-entrypoint-initdb.d/02-seed.sql`

To trigger auto-init (⚠️ wipes DB data):

```bash
docker compose down -v
docker compose up -d
docker compose logs db # look for 01-schema.sql / 02-seed.sql
```

### Manual

If you’re not resetting the volume, run the scripts manually.

Inside the DB container shell:

```bash
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f /sql/init/schema.sql
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f /sql/init/seed.sql
```

---

## Run prepared queries & capture results

Use `\o` to direct output into the **writeable** path `/sql/results/` (which maps to `sql/results/` on your host).

```sql
-- Q1 example
\o /sql/results/q1.txt
\i /sql/queries/q1.sql
\o
```

Repeat for `q2.sql` … `q8.sql`.

### EXPLAIN before/after (index demo)

```sql
-- BEFORE
\o /sql/results/q6_before.txt
\i /sql/queries/q6_before.sql
\o

-- Create index (safe if exists)
CREATE INDEX IF NOT EXISTS idx_tasks_user_id ON tasks(user_id);

-- AFTER
\o /sql/results/q6_after.txt
\i /sql/queries/q6_after.sql
\o
```

> Tip: use `EXPLAIN (ANALYZE, BUFFERS)` in the `q6_*` scripts to view real timings and buffer usage.

The files will appear in `sql/results/` in the repo.

---

## Useful one-liners (still use container env)

If you prefer not to enter an interactive shell, run a one-liner that starts a shell **inside** the container and then
calls `psql`:

List tables:

```bash
docker compose exec db bash -lc 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "\dt"'
```

Run a single query:

```bash
docker compose exec db bash -lc \
'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "SELECT id, title FROM tasks ORDER BY id LIMIT 5;"'
```

Export query output to a file:

```bash
docker compose exec db bash -lc \
'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "\o /sql/results/q7.txt" -c "\i /sql/queries/q7.sql" -c "\o"'
```

---

## Troubleshooting

**Port 5432 already in use**
Another Postgres is running locally. Stop it or change `POSTGRES_PORT` in `.env`, then:

```bash
docker compose down
docker compose up -d
```

**Tables missing after start**

* If this is a fresh volume and auto-init is enabled, ensure you started from a clean slate (`docker compose down -v`).
* Otherwise, run `schema.sql` and `seed.sql` manually.

**Env vars look wrong**

* Run `docker compose config` from the repo root to see the interpolated values.
* Remember: the recommended flow is to open a shell **inside** the container and run `psql` there.

**Cannot write to results**

Ensure the folder exists:

```bash
mkdir -p sql/results
```
