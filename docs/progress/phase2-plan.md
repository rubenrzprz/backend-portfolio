Excellent — here’s the companion file you can place at:

```
docs/progress/phase2-plan.md
```

This is designed to follow the same tone and formatting as your previous progress docs and transition cleanly from **Phase 1 → Phase 2**.

---

# 🚀 Phase 2 Plan – November 2025

**Period:** 2025-11-03 → 2025-11-30
**Status:** 🟡 In Progress

---

## 🎯 Objective

Build **2 showcase-level backend projects** to demonstrate intermediate and production-grade Spring Boot skills:

1. **ResHub** – Hotel Reservation Management System (main focus).
2. **StockBox** – AI-assisted stock and market insights API.

Each project should feature clean architecture, testing discipline, and CI/CD workflows, extending the portfolio toward employable quality.

---

## 🧱 Core Focus Areas

| Area                    | Goals                                                                                   |
| ----------------------- | --------------------------------------------------------------------------------------- |
| **Spring Boot Mastery** | Multi-layer structure, validation, pagination/filtering, error handling, modular design |
| **Database Design**     | Entity relations, migrations, seeding, optimization                                     |
| **Auth & Security**     | JWT authentication, RBAC roles (MANAGER / RECEPTIONIST / AGENCY)                        |
| **Testing**             | Integration + containerized DB tests with Testcontainers                                |
| **CI/CD**               | GitHub Actions pipelines for build/test + coverage reports                              |
| **Documentation**       | Updated API docs, Postman collections, architecture diagrams                            |
| **Professional Growth** | Public milestones, resume-ready project outcomes                                        |

---

## 📆 4-Week Breakdown

### 🗓️ Week 1 – ResHub Foundation (Nov 3–9)

**Goal:** Set up a clean scaffold and working environment.

* [ ] Create new repo `rubenrzprz/reshub` (MIT License).
* [ ] Initialize Spring Boot project (Java 21, Maven).
* [ ] Add `.gitignore`, basic README, and first commit.
* [ ] Define project structure (controller/service/repository/domain).
* [ ] Add `/health` endpoint and verify build/test locally + in CI.
* [ ] Link **ResHub** in backend-portfolio README.

### 🗓️ Week 2 – Core Domain & Persistence (Nov 10–16)

**Goal:** Implement domain entities and database integration.

* [ ] Define entities: `Hotel`, `Room`, `Booking`, `User`, `Role`.
* [ ] Add PostgreSQL config and schema migration via Flyway or Liquibase.
* [ ] Implement CRUD for core entities with proper validation.
* [ ] Add test coverage with Testcontainers.
* [ ] Document API endpoints in `docs/api.md`.

### 🗓️ Week 3 – Auth, RBAC & Business Logic (Nov 17–23)

**Goal:** Implement security and business workflows.

* [ ] Add JWT-based authentication (Spring Security).
* [ ] Create role-based access control (MANAGER / RECEPTIONIST / AGENCY).
* [ ] Implement booking workflow with availability checks.
* [ ] Expand Postman collection for full flow.
* [ ] Add CI badge and badge group to README.

### 🗓️ Week 4 – Docs, Polish & StockBox Kickoff (Nov 24–30)

**Goal:** Finalize ResHub and prepare the next project.

* [ ] Refactor packages into feature-based structure.
* [ ] Polish README + architecture diagram (Mermaid).
* [ ] Record short demo (GIF or CLI test proof).
* [ ] Create StockBox repository and basic README scaffold.
* [ ] Write short end-of-phase reflection summary.

---

## 🧠 Learning Objectives

By the end of Phase 2, you should confidently:

* Design and secure full-featured REST APIs.
* Manage persistence, migrations, and testing with PostgreSQL.
* Automate builds, tests, and coverage in CI.
* Present a **cohesive backend portfolio** demonstrating readiness for junior backend roles.

---

## 🧩 Deliverables

* ✅ `rubenrzprz/reshub` (main showcase project).
* ✅ `rubenrzprz/stockbox` (secondary supporting project).
* ✅ Updated backend-portfolio README linking both.
* ✅ Documented weekly summaries (`progress/day15–day28-summary.md`).
* ✅ Phase 2 reflection + readiness checklist.

---

## 🔗 Related

* [Phase 1 Checklist](./phase1-checklist.md)
* [ResHub Repository](https://github.com/rubenrzprz/reshub)

---

Would you like me to also generate the **Phase 2 kickoff summary (Day 14 – Summary)** next, to formally close Phase 1 and start Phase 2 in your repo?
