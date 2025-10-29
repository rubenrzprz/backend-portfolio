# Day 4 – Summary

**Date:** 2025-10-23
**Status:** ✅ Completed

---

## 🎯 Objectives

* Introduce **Spring Boot** and run the service as a web app
* Expose two deterministic endpoints: `GET /health`, `GET /items`
* Add focused **Web MVC** tests for the controllers
* Commit reproducible **Postman** artifacts (environment + collection)
* Document copy-paste **cURL** and local run instructions

---

## 🧩 Tasks Completed

* **Build & bootstrap**

    * `chore(build)`: added Spring Boot (web starter + plugin) to `pom.xml`
    * `feat(api)`: added `BackendPortfolioApplication` (Spring Boot entrypoint)
* **Endpoints**

    * `feat(api)`: implemented controllers:

        * `GET /health` → `{ "status": "ok", "service": "backend-portfolio" }`
        * `GET /items` → static list `[{id:1,name:"alpha"},{id:2,name:"beta"}]`
* **Tests**

    * `test(api)`: added `@WebMvcTest` slice tests for `/health` and `/items`
* **Cleanup**

    * `chore(cleanup)`: removed the old CLI `HelloBackend` entrypoint
* **Artifacts & docs**

    * `docs(postman)`: committed `postman/environment.json` and `postman/collection.json`
    * `docs(api)`: created `docs/api.md` with base URL, cURL, expected JSON, and run instructions

* **Standards & tooling**

    * `fix(git): **fixed** .gitmessage **encoding** (UTF-8 LF, no BOM) and set as the repo’s commit template`

---


## 🛠️ Commands Used

```bash
# run locally
mvn -q spring-boot:run

# verify endpoints
curl -s http://localhost:8080/health
curl -s http://localhost:8080/items

# run tests
mvn -q test
```

---

## 🧠 Key Learnings

* **`@WebMvcTest`** focuses on the web layer (controllers, routing, JSON, validation) without booting the full app → fast and precise HTTP contract testing.
* Keep responses **deterministic** (no timestamps/random) so tests and docs don’t drift.
* Separate concerns into **atomic commits** (build → entrypoint → endpoints → tests → cleanup → docs) to keep history readable and rollback simple.
* Commit templates must be plain UTF-8 (no BOM) with LF endings to avoid weird characters in Git.

---

## 🚀 Next Steps

* Extend `/items` into **CRUD** (Create/Read/Update/Delete).
* Add **validation** (400) and a **global error contract** for 400/404/500 with slice tests.
* Begin preparing DB integration for Day 6–7 (schemas + migrations).

---

## 📂 Deliverables

* `pom.xml` (Spring Boot web + plugin)
* `src/main/java/com/ruben/backendportfolio/BackendPortfolioApplication.java`
* `src/main/java/com/ruben/backendportfolio/api/HealthController.java`
* `src/main/java/com/ruben/backendportfolio/api/ItemsController.java`
* `src/test/java/com/ruben/backendportfolio/api/HealthControllerTest.java`
* `src/test/java/com/ruben/backendportfolio/api/ItemsControllerTest.java`
* `postman/environment.json`
* `postman/collection.json`
* `.gitmessage` (UTF-8 LF)
* `docs/api.md`
* `progress/day4-summary.md`
