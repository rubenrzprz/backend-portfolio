# Day 8 – Summary

**Date:** 2025-10-27
**Status:** ✅ Completed

---

## 🎯 Objectives

* Containerize the **Spring Boot API** and run it alongside **PostgreSQL** via Docker Compose.
* Make configuration **environment-driven** so local, Docker, and future CI runs “just work.”
* Ensure a **reproducible DB state** (schema + seed) and update docs/Postman to match.

---

## 🧩 Tasks Completed

* **Compose & Docker**

    * Moved **`docker-compose.yml`** to repo root; standardized env loading via root `.env`.
    * Added **multi-stage Dockerfile** and `app` service to run the API containerized.
    * Default dev flow now brings up **API + DB** with `docker compose up -d --build`.
    * DB-only mode documented (`docker compose up -d db`) for running the app **locally**.

* **Config**

    * Refactored datasource to be **env-driven** in `application.properties`:

        * Prefers `SPRING_DATASOURCE_URL`; otherwise composes from `POSTGRES_*` with sane defaults.
    * `application-docker.properties` now only overrides host to `db`.

* **Database init**

    * Enabled **auto-init** on fresh volumes by mounting:

        * `sql/init/schema.sql` → `/docker-entrypoint-initdb.d/01-schema.sql`
        * `sql/init/seed.sql` → `/docker-entrypoint-initdb.d/02-seed.sql`

* **Docs**

    * Updated **`docs/api.md`**: run modes (API+DB vs DB-only), env-driven notes, and **Postman usage**.
    * Updated **`docs/sql.md`**: root-level Compose, **container shell → psql** workflow, auto-init behavior.

* **Postman**

    * Updated **`postman/collection.json`** with tests (status, headers), and `itemId` chaining across CRUD.

---

## 🛠️ Commands Used

```bash
# 1) Default dev: API + DB (containerized)
docker compose up -d --build
curl http://localhost:8080/health

# 2) DB-only (run app locally)
docker compose up -d db
# In another terminal:
mvn -q -Dspring-boot.run.profiles=local spring-boot:run

# 3) Force a clean DB (triggers auto-init; ⚠ wipes data)
docker compose down -v
docker compose up -d db
docker compose logs db | grep docker-entrypoint-initdb.d
```

**Postman:**

* Import `postman/collection.json` (and your environment with `baseUrl=http://localhost:8080` if you use one).
* Run requests in order: Health → Create → List → Get → Put → Patch → Delete → 400/404 cases.

---

## 🧠 Key Learnings

* **Env-driven config** keeps one source of truth across local, Docker, and CI.
* **Auto-init** eliminates first-run schema issues; manual scripts remain available.
* **Two run modes** (API+DB default, DB-only for local app) make dev flows explicit and easy.
* Postman tests provide a quick, **repeatable verification** of the HTTP contract.

---

## 🚀 Next Steps

* **[Task] Refactor packages into subpackages** (feature-first layout for `items`, tidy exceptions/validation).
* Add a short “Run with Docker” blurb to the repo **README** (linking to the updated docs).
* Consider adding CI (GitHub Actions) to build/test on push.

---

## 📂 Deliverables

* `docker-compose.yml` at repo root (API + DB), multi-stage **Dockerfile** for the app.
* `src/main/resources/application.properties` (env-driven), `application-docker.properties` (host override).
* Auto-init mounts wired for `schema.sql` and `seed.sql`.
* Updated docs: `docs/api.md`, `docs/sql.md`.
* Updated Postman collection: `postman/collection.json`.
