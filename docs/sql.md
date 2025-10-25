# SQL — Local PostgreSQL (Docker-only, no native client)

This guide explains how to run PostgreSQL **in Docker**, apply schema/seed scripts, execute queries, and capture results — all **from inside the container**.
It reflects the current repo setup:

* `.env` lives at the **repo root** (not committed).
* Docker Compose file lives in **`sql/docker-compose.yml`**.
* The container mounts:

    * the whole **`sql/`** folder **read-only** at `/sql`
    * **`sql/results/`** **read-write** at `/sql/results` (so query outputs are saved to your host)
* A named volume `bp_pg_data` persists database files between restarts.

---

## Prerequisites

* **Docker Desktop** (includes Docker Engine + Compose v2)
* A root **`.env`** file (create it from the example)

Create `.env` at the repo root:

```bash
cp .env.example .env
# Edit if needed; defaults are safe for local dev
```

`.env` keys used:

```
POSTGRES_USER=bp_user
POSTGRES_PASSWORD=bp_pass
POSTGRES_DB=bp_db
POSTGRES_PORT=5432
```

> `.env` is ignored by Git. Only `.env.example` is committed.

---

## Compose layout & mounts (what’s inside the container)

From `sql/docker-compose.yml`:

* `./:/sql:ro` → Your repo’s `sql/` directory is **read-only** at `/sql`
* `./results:/sql/results:rw` → Only `sql/results/` is **writeable** (for `\o` outputs)
* `bp_pg_data:/var/lib/postgresql/data` → **Named Docker volume** that persists your database files

**Implications**

* Read scripts via **`/sql/...`** (e.g., `/sql/init/schema.sql`)
* Write outputs to **`/sql/results/...`** (e.g., `/sql/results/q1.txt`)
* A normal `down` **keeps** your data; `down -v` **resets** it

---

## Start / Stop the database

> Run these from the **repo root** so the relative compose path works.
> We always pass the root env file with `--env-file ./.env`.

**Start (detached):**

```bash
docker compose -f sql/docker-compose.yml --env-file ./.env up -d
```

**Check status:**

```bash
docker compose -f sql/docker-compose.yml --env-file ./.env ps
```

**Stop (keep data):**

```bash
docker compose -f sql/docker-compose.yml --env-file ./.env down
```

**Stop + remove volume (full reset):**

```bash
docker compose -f sql/docker-compose.yml --env-file ./.env down -v
```

---

## Connect with `psql` (inside the container)

Open a shell in the `db` service:

```bash
docker compose -f sql/docker-compose.yml --env-file ./.env exec db bash
```

Connect to Postgres:

```bash
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"
-- You should see a prompt like:  bp_db=#
```

> If env vars aren’t visible inside the shell, you can use explicit values:
> `psql -U bp_user -d bp_db`

---

## Apply schema & seed

From the `psql` prompt (inside the container):

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
└─ results/                  ← writeable mount (files created here by \o)
   ├─ q1.txt … q8.txt
   ├─ q6_before.txt
   └─ q6_after.txt
```

---

## Useful commands

```bash
# Start DB (uses root .env)
docker compose -f sql/docker-compose.yml --env-file ./.env up -d

# Shell into the DB container
docker compose -f sql/docker-compose.yml --env-file ./.env exec db bash

# One-off SQL from host (no shell), e.g., count rows
docker compose -f sql/docker-compose.yml --env-file ./.env exec -T db \
  psql -U bp_user -d bp_db -c "SELECT COUNT(*) FROM users;"
```

---

## Troubleshooting

* **“variable not set” warnings**
  
Always pass the env file when running Compose commands:

  ```
  docker compose -f sql/docker-compose.yml --env-file ./.env <cmd>
  ```

* **Schema/seed path not found**
  
Remember scripts live at **`/sql/...`** inside the container:

  ```
  \i /sql/init/schema.sql
  \i /sql/init/seed.sql
  ```

* **Cannot write outputs**
  
Write to the **mounted writeable path**:

  ```
  \o /sql/results/q1.txt
  ```

* **Port 5432 in use**
  
Change `POSTGRES_PORT` in `.env` (e.g., `5433`), then restart:

  ```
  docker compose -f sql/docker-compose.yml --env-file ./.env down
  docker compose -f sql/docker-compose.yml --env-file ./.env up -d
  ```

* **Reset database to a clean state**
  
Use `down -v` to remove the named volume:

  ```
  docker compose -f sql/docker-compose.yml --env-file ./.env down -v
  ```

---

## Why we use a named volume (`bp_pg_data`)

* Persists database files across container restarts.
* You only lose data if you explicitly run `down -v`.
* Safer and cleaner than bind-mounting Postgres internals into your repo.

---

## Good practices

* Never commit real credentials. Keep `.env` local; commit `.env.example` only.
* Store all outputs under `sql/results/` for reproducibility and review.
