# Day 3 – Summary

**Date:** 2025-10-22
**Status:** ✅ Completed

---

## 🎯 Objectives

* Practice disciplined branching with **5 atomic commits** on a tiny Java project
* Learn local **sync strategy**: rebase (preferred) vs merge (learning only)
* Codify repo workflow in **GIT_GUIDELINES.md**
* Add **.gitignore** to keep the repo clean
* Add **LICENSE (MIT)** to clarify reuse terms
* Set **repository topics** for discoverability

---

## 🧩 Tasks Completed

* **Micro-commit exercise (Java/Maven)**

    * `chore(init)`: scaffold minimal Maven project (Java 21) + entrypoint
    * `feat(core)`: `HelloService.greet()` with timestamp
    * `refactor(core)`: injected `Clock` for deterministic tests
    * `test(core)`: JUnit test with fixed clock
    * `docs(progress)`: Day 3 notes + run/test commands
* **Branching & sync**

    * Practiced `git pull --rebase` to keep feature history linear
    * Merged the practice PR via **Squash & merge**
* **Standards & hygiene**

    * Added **GIT_GUIDELINES.md** (branch naming, atomic commits, rebase-preferred, Squash & merge policy, cleanup)
    * Added **.gitignore** for Java/Maven/IntelliJ/OS artifacts
    * Added **LICENSE (MIT)** at repo root
* **Repository topics**

    * Topics set on the repo: `java`, `spring-boot`, `rest-api`, `sql`, `docker` *(and any others you added)*

---

## 🛠️ Tools & Commands Used

* **Git**: feature branches, `rebase`, squash merges, branch cleanup
* **Java 21 + Maven**: `mvn -q package`, `mvn -q test`
* **JUnit 5** for unit tests

```bash
mvn -q -DskipTests package
java -cp target/classes com.ruben.backendportfolio.HelloBackend
mvn -q test
git log --oneline --graph
```

---

## 🧠 Key Learnings / Insights

* **Rebase locally** keeps a clean, linear history on feature branches; understand merges, but don’t push merge commits into `main`.
* Put **closing keywords** (e.g., `Closes #…`) in the **PR description** so they survive squash merges.
* **Atomic commits** (init → feat → refactor → test → docs) make review and rollback straightforward.
* Early repo hygiene (**.gitignore**, **LICENSE**) reduces noise and clarifies expectations.

---

## 🚀 Next Steps

* **Day 4**: evolve this project into a **Spring Boot** API with `GET /health` and `GET /items`; export a **Postman** collection and add example requests.
* **Carryover (tiny fix):** resolve the **`.gitmessage` UTF-8** encoding bug via a 1-file PR.
  `Refs #20`.

---

## 📂 Deliverables

* `pom.xml`
* `src/main/java/com/ruben/backendportfolio/HelloBackend.java`
* `src/main/java/com/ruben/backendportfolio/HelloService.java`
* `src/test/java/com/ruben/backendportfolio/HelloServiceTest.java`
* `GIT_GUIDELINES.md`
* `.gitignore`
* `LICENSE`
* `progress/day3-summary.md`