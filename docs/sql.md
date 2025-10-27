# SQL & Local Database Guide

Run **PostgreSQL** for this repo using **Docker Compose** (from the **repo root**), load schema/seed data, run queries, and capture results—without installing Postgres locally.

---

## Prerequisites

* Docker Desktop / Docker Engine
* Docker Compose (bundled with recent Docker)

> We run `psql` **inside** the container, so you don’t need a local client.

---

## Environment & `.env`

Because `docker-compose.yml` is at the **repo root**, Docker Compose **auto-loads** the `.env` file in the **same folder**. You can usually run commands **without** `--env-file`.

**Defaults (see `.env.example`):**

```
POSTGRES_USER=bp_user
POSTGRES_PASSWORD=bp_pass
POSTGRES_DB=bp_db
POSTGRES_PORT=5432
```

> **Note:** You can still be explicit if you want:
> `docker compose --env-file ./.env up -d`
> Or use an alternate env file (e.g., CI):
> `docker compose --env-file ./.env.ci up -d`

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

## Start / Stop / Reset (from repo root)

Start Postgres:

```bash
docker compose up -d
```

See running containers:

```bash
docker compose ps
```

Stop (keeps the data volume):

```bash
docker compose down
```

Reset **everything** (containers + named volumes; **wipes DB data**):

```bash
docker compose down -v
```

Preview resolved config (shows env interpolation):

```bash
docker compose config
```

---

## Connect with psql (inside the container)

> We always open a shell **inside** the container first, then run `psql`.
> This ensures credentials come from the **container** environment, not your host.

Open a shell:

```bash
docker compose exec db bash
```

Then run `psql` inside the container:

```bash
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"
-- You should see a prompt like:  bp_db=#
```

Exit with `\q`.

---

## Load schema & seed (manual)

From the psql prompt (inside the container):

```sql
-- apply schema and seed from the read-only mount
\i /sql/init/schema.sql
\i /sql/init/seed.sql

-- quick checks
SELECT COUNT(*) AS users FROM users;
SELECT COUNT(*) AS tasks FROM tasks;
```

---

## Run queries & capture results

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

If you prefer not to enter an interactive shell, you can run a one-liner that **starts a shell inside the container** (so env vars resolve there) and then calls `psql`:

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

* If this is a fresh volume, you still need to run `schema.sql` and `seed.sql` **manually** (see above).
  *(Auto-init is not enabled in this repo at the moment.)*

**Env vars look wrong**

* Run `docker compose config` from the repo root to see the interpolated values.
* Remember: the recommended flow is to open a shell **inside** the container and run `psql` there.

**Cannot write to results**

Ensure the folder exists:

```bash
mkdir -p sql/results
```

## Why we use a named volume (`bp_pg_data`)

* Persists database files across container restarts.
* You only lose data if you explicitly run `down -v`.
* Safer and cleaner than bind-mounting Postgres internals into your repo.

---

## Good practices

* Never commit real credentials. Keep `.env` local; commit `.env.example` only.
* Store all outputs under `sql/results/` for reproducibility and review.