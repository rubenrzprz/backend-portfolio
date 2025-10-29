# Day 6 – Summary

**Date:** 2025-10-25
**Status:** ✅ Completed

---

## 🎯 Objectives

* Stand up a **Docker-only** PostgreSQL workflow using the **root `.env`**.
* Create a minimal **schema** (`users`, `tasks`) and **seed** data.
* Write and run **8 focused SQL queries**, including an **EXPLAIN before/after** index comparison.
* Capture **reproducible outputs** under `sql/results/` and document the process.

---

## 🧩 Tasks Completed

* **Docker & env setup**

    * `chore(docker)`: added `sql/docker-compose.yml` with:

        * Named volume **`bp_pg_data`** for persistent data.
        * Bind mounts: `sql/` **read-only** at `/sql` and `sql/results/` **read-write** at `/sql/results`.
    * `chore(env)`: added **`.env.example`** (root) and used root `.env` via `--env-file ./.env`.

* **Documentation**

    * `docs(sql)`: updated **`docs/sql.md`** for a **container-only** `psql` flow, root `.env`, mounts, and reset instructions.
    * `docs(sql)`: pointer **`sql/README.md`** → `docs/sql.md`.

* **Schema & seed**

    * `feat(sql)`: **`sql/init/schema.sql`**

        * `users(id BIGINT IDENTITY, name, email UNIQUE, created_at TIMESTAMPTZ)`
        * `tasks(id BIGINT IDENTITY, user_id FK → users.id ON DELETE CASCADE, title, status CHECK('todo'|'doing'|'done'), created_at TIMESTAMPTZ)`
        * Indexes: `idx_tasks_user_id`, `idx_tasks_created_at`.
    * `feat(sql)`: **`sql/init/seed.sql`** with 4 users (including one with **0 tasks**) and varied task statuses/timestamps.

* **Queries & outputs**

    * `docs(sql)`: **`sql/queries/q1.sql … q8.sql`** including `q6_before.sql`/`q6_after.sql` for the index plan comparison.
    * Captured outputs under **`sql/results/`**: `q1.txt … q8.txt` + `q6_before.txt` / `q6_after.txt`.

* **Process**

    * Worked on **`feature/sql-fundamentals`** via a **Draft PR**, pushing small atomic commits.
    * All commands executed inside the container (no native client).

---

## 🛠️ Commands Used

```bash
# start/stop DB (always from repo root)
docker compose -f sql/docker-compose.yml --env-file ./.env up -d
docker compose -f sql/docker-compose.yml --env-file ./.env ps
docker compose -f sql/docker-compose.yml --env-file ./.env down        # keep data
docker compose -f sql/docker-compose.yml --env-file ./.env down -v     # reset data

# connect inside the container and open psql
docker compose -f sql/docker-compose.yml --env-file ./.env exec db bash
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"

# apply schema & seed (inside psql)
\i /sql/init/schema.sql
\i /sql/init/seed.sql

# run queries and capture results (inside psql)
\o /sql/results/q1.txt
\i /sql/queries/q1.sql
\o

# EXPLAIN before/after index (inside psql)
\o /sql/results/q6_before.txt
\i /sql/queries/q6_before.sql
\o
\o /sql/results/q6_after.txt
\i /sql/queries/q6_after.sql
\o
```

---

## 🧠 Key Learnings

* **Compose-time vs container-time env**: `${VAR:-default}` affects compose parsing (e.g., `ports`), while `env_file` and `environment` define what the container actually sees.
* **Mount strategy matters**: keep scripts **read-only**; expose a **writeable** results folder for `\o` to ensure reproducible artifacts without risking accidental in-container edits.
* **Indexes & EXPLAIN**: creating `idx_tasks_user_id` changes plans from potential `Seq Scan` to an `Index Scan` when predicates line up (`WHERE user_id = ?`). Use `EXPLAIN (ANALYZE, BUFFERS)` to see real costs.
* **Data modeling choices**: `BIGINT IDENTITY` plays nicely with Java `Long`; `TIMESTAMPTZ` is safer for real systems; `CHECK` constraints document valid state simply.
* **Atomic commits + Draft PR** keep progress visible while preserving a readable history.

---

## 🚀 Next Steps

* **Day 7:** integrate Postgres with the API (Spring Data/JPA), replace in-memory store; add `@DataJpaTest` for repositories.
* **Refactor intention:** reorganize `items` packages into `web/ dto/ domain/ service/ repo/` as noted earlier (kept flat until DB integration lands).
* Optional: consider **Testcontainers** later for isolated integration tests in CI.

---

## 📂 Deliverables

* **Docker & env**

    * `sql/docker-compose.yml`
    * `.env.example` (root)

* **Docs**

    * `docs/sql.md`
    * `sql/README.md` (pointer)

* **Schema & seed**

    * `sql/init/schema.sql`
    * `sql/init/seed.sql`

* **Queries**

    * `sql/queries/q1.sql`
    * `sql/queries/q2.sql`
    * `sql/queries/q3.sql`
    * `sql/queries/q4.sql`
    * `sql/queries/q5.sql`
    * `sql/queries/q6_before.sql`
    * `sql/queries/q6_after.sql`
    * `sql/queries/q7.sql`
    * `sql/queries/q8.sql`

* **Results**

    * `sql/results/q1.txt`
    * `sql/results/q2.txt`
    * `sql/results/q3.txt`
    * `sql/results/q4.txt`
    * `sql/results/q5.txt`
    * `sql/results/q6_before.txt`
    * `sql/results/q6_after.txt`
    * `sql/results/q7.txt`
    * `sql/results/q8.txt`
