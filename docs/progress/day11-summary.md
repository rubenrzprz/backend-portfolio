# Day 11 – Summary

**Date:** 2025-10-30
**Status:** ✅ Completed

---

## 🎯 Objectives

* Automate **build and test execution** on every push and pull request using **GitHub Actions**.
* Ensure a **PostgreSQL 16 service** spins up, gets **auto-seeded** from the repo SQL files, and integrates smoothly with Maven tests.
* Add a **CI badge** and document the setup for transparency and reproducibility.

---

## 🧩 Tasks Completed

* **Workflow Setup**

    * Created `.github/workflows/ci.yml` configured to trigger on:

        * Any **push** (all branches),
        * **Pull requests** targeting `main`,
        * **Manual dispatch** (`workflow_dispatch`).
    * Configured **Temurin JDK 21** with Maven caching via `actions/setup-java@v4`.
    * Added workflow **concurrency** and set timezone for consistent timestamps.

* **PostgreSQL Service & Seeding**

    * Added **Postgres 16** service with proper healthcheck.
    * Seeded database using repo SQL files:

        * `sql/init/schema.sql`
        * `sql/init/seed.sql`
          Executed via `psql` command before running tests.
    * Ensured credentials are pulled from **Actions Secrets/Variables**:

        * Secrets → `CI_DB_USER`, `CI_DB_PASS`
        * Variables → `CI_DB_NAME=bp_db`, `CI_DB_PORT=5432`

* **Testing & Artifacts**

    * Ran tests using:

      ```bash
      mvn -B -ntp clean verify
      ```
    * Uploaded **Surefire/Failsafe reports** as an artifact (`test-reports/`).

* **README Update**

    * Added CI badge:
      `[![Java CI with Maven](https://github.com/rubenrzprz/backend-portfolio/actions/workflows/ci.yml/badge.svg)](https://github.com/rubenrzprz/backend-portfolio/actions/workflows/ci.yml)`
    * Inserted **Continuous Integration (GitHub Actions)** section right after “Repository Structure.”
    * Described workflow steps, secrets, and artifact handling.

* **Verification**

    * Triggered the workflow via push and confirmed:

        * Postgres healthcheck **passed**.
        * SQL seeding **executed successfully**.
        * Maven test suite **completed without errors**.
        * Artifact uploaded and accessible under the Actions tab.
    * Opened and merged **PR #50** after successful checks.

---

## 🧠 Key Learnings

* Using **Actions services** for databases enables full integration testing in CI, mirroring local dev behavior.
* Proper **secret management** avoids exposing sensitive data and keeps workflows reusable.
* Uploading test reports as artifacts helps track **historical build health** without bloating the repo.

---

## 📂 Deliverables

* `.github/workflows/ci.yml` — complete CI pipeline (JDK 21, Postgres, seeded tests).
* Updated **README.md** with CI badge and documentation section.
* Verified CI run and merged **PR #50** with green checks and uploaded artifacts.
