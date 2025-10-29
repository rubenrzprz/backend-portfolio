# Day 7 – Summary

**Date:** 2025-10-26
**Status:** ✅ Completed

---

## 🎯 Objectives

* Replace in-memory `/items` storage with **PostgreSQL** via **Spring Data JPA**, keeping the HTTP contract unchanged.
* Route all create/read/update/delete operations through a proper **repository** implementation.
* Ensure local developer experience remains simple (run with a **local profile**).

---

## 🧩 Tasks Completed

* **Dependencies & config**

    * `chore(deps)`: added **Spring Data JPA** and **PostgreSQL JDBC** to `pom.xml`.
    * `chore(config)`: added `src/main/resources/application-local.properties` with safe defaults for local dev (pairs with our Docker Postgres).

* **Persistence layer**

    * `feat(data)`: created **`TaskEntity`** (maps to `tasks` table) and **`TaskJpaRepository`**.
    * `feat(data)`: implemented **`JpaItemsRepository`** (marked `@Primary`) that maps `Item ↔ TaskEntity` and uses **upsert** semantics in `save(Item)`.

* **Service wiring**

    * `refactor(service)`: **stopped generating IDs** in the service; now all creates/updates go through `repo.save(Item)`.
    * removed now-unused `idSeq`.

* **Manual verification**

    * Verified **POST/GET/PUT/PATCH/DELETE** flows against the live DB with expected status codes (`201/200/204/404`).

---

## 🛠️ Commands Used

```bash
# 1) Start Postgres (uses root .env)
docker compose -f sql/docker-compose.yml --env-file ./.env up -d
docker compose -f sql/docker-compose.yml --env-file ./.env ps

# 2) Run the app with local profile
# PowerShell (Windows)
mvn --% -Dspring-boot.run.profiles=local spring-boot:run
# macOS/Linux alternative:
# mvn -q -Dspring-boot.run.profiles=local spring-boot:run
```

**Manual checks (Postman/curl):**

* `POST /items` `{ "name": "Alpha" }` → **201** + `Location` header
* `GET /items` → list includes “Alpha”
* `GET /items/{id}` (existing) → **200**; (missing) → **404**
* `PUT /items/{id}` `{ "name": "Beta" }` → **200**
* `PATCH /items/{id}` `{ "name": "Gamma" }` → **200**
* `DELETE /items/{id}` → **204**; `DELETE /items/999999` → **404**

---

## 🧠 Key Learnings

* **Separation of concerns:** Controllers stay HTTP-focused; **service** enforces rules (404 on missing), and **repository** handles persistence.
* **Upsert via `save(Item)`:** With JPA, `save` naturally inserts or updates based on ID; the **service** decides semantics (e.g., 404 for missing on PUT/PATCH).
* **DX matters:** A dedicated **`application-local.properties`** keeps “clone → run” easy while production/CI can later use env-driven config.
* **Id generation belongs to the DB:** Removing client-side ID generation avoids collisions and keeps truth in the datastore.

---

## 🚀 Next Steps

* **[Task] Load SQL init scripts via Docker entrypoint (schema + seed):** auto-create tables on fresh volumes for new contributors.
* **[Task] Refactor Spring datasource to env-driven config:** keep `local` profile for DX; env overrides for CI/Prod.
* **(Planned)** Package reorg after DB landing (`items/{web,dto,domain,service,repo}`) for clarity.
* **(Later)** Add `@DataJpaTest` coverage for repository behavior; consider Testcontainers for CI isolation.

---

## 📂 Deliverables

* `pom.xml` updated with **JPA** and **PostgreSQL** dependencies
* `src/main/resources/application-local.properties` (local DS config)
* `src/main/java/.../data/TaskEntity.java`
* `src/main/java/.../data/TaskJpaRepository.java`
* `src/main/java/.../items/JpaItemsRepository.java` (primary adapter)
* `src/main/java/.../items/ItemService.java` updated to route through repository (no local ID gen)
