# Day 5 – Summary

**Date:** 2025-10-24
**Status:** ✅ Completed

---

## 🎯 Objectives

* Implement full **CRUD** for `/items` with correct HTTP semantics.
* Enforce **validation** and add a **global error contract** (400/404/500).
* Add focused **Web MVC tests** (happy paths + error cases).
* Update **API docs** and **Postman collection** (id chaining).
* Tidy up validation messages and deduplicate rules.

---

## 🧩 Tasks Completed

* **Dependencies & setup**

    * `chore(deps)`: added Spring Validation starter (Jakarta Bean Validation + MVC integration).

* **Domain & DTOs**

    * `feat(api)`: added `Item` domain model (record).
    * `feat(api)`: added `ItemCreateRequest` and `ItemUpdateRequest` with `@NotBlank` + `@Size`.

* **Repository & Service**

    * `feat(api)`: added in-memory `ItemRepository` + `InMemoryItemRepository`.
    * `feat(api)`: added `ItemService` (AtomicLong ID gen, list/get/create/replace/patch/delete).

* **Controller**

    * `feat(api)`: added `ItemsController` with:

        * `POST /items` → **201 Created** + `Location: /items/{id}`
        * `GET /items` → **200 OK**
        * `GET /items/{id}` → **200 OK** (mapped to **404** by handler if missing)
        * `PUT /items/{id}` → **200 OK**
        * `PATCH /items/{id}` → **200 OK** (name-only today)
        * `DELETE /items/{id}` → **204 No Content**

* **Error handling**

    * `refactor(errors)`: introduced `ItemNotFoundException` (dedup message, keep `id`).
    * `feat(api)`: added `GlobalExceptionHandler` (+ `ApiError`) to map:

        * **400** validation → `validation_error` with `details[]`
        * **404** not found → `not_found` with message + path
        * **500** generic → `internal_error`

* **Testing**

    * `test(api)`: added `@WebMvcTest` suites:

        * **Happy paths:** create/list/get/put/patch/delete (assert status, headers, JSON).
        * **Errors:** invalid name → **400**; unknown id → **404**.

* **Docs & tooling**

    * `docs(api)`: updated `docs/api.md` (CRUD examples, headers, error bodies).
    * `docs(postman)`: updated `postman/collection.json` (captures `itemId` after POST; basic tests).
    * `chore(cleanup)`: removed legacy `api.ItemsController` and its test to fix bean-name conflict.
    * `chore(i18n)`: added `messages.properties`; `feat(validation)`: added `@ValidName` composed constraint; refactored DTOs to use it.

* **Process**

    * Worked via **Draft PR**, pushed small commits, updated checklist, then **Squash & merge**.

---

## 🛠️ Commands Used

```bash
# run locally
mvn -q spring-boot:run

# quick HTTP checks
curl -s -i -X POST http://localhost:8080/items -H 'Content-Type: application/json' -d '{ "name":"alpha" }'
curl -s http://localhost:8080/items
curl -s http://localhost:8080/items/1
curl -s -i -X PUT   http://localhost:8080/items/1 -H 'Content-Type: application/json' -d '{ "name":"alpha-2" }'
curl -s -i -X PATCH http://localhost:8080/items/1 -H 'Content-Type: application/json' -d '{ "name":"alpha-3" }'
curl -s -i -X DELETE http://localhost:8080/items/1

# error paths
curl -s -i -X POST http://localhost:8080/items -H 'Content-Type: application/json' -d '{ "name": "" }'   # 400
curl -s -i http://localhost:8080/items/999999                                                            # 404

# tests
mvn -q test
```

---

## 🧠 Key Learnings

* Keep **status codes explicit** in controllers; centralize **error mapping** with `@ControllerAdvice`.
* DTO **validation at the edge** (`@Valid`) prevents invalid data from reaching services.
* **Records** are ideal for small immutable DTOs/domain carriers; concise and JSON-friendly.
* Use **message keys** in `messages.properties` (and optional `@ValidName`) to avoid duplication and enable i18n.
* Maintain a **Draft PR** with a progress checklist for visibility; **Squash & merge** keeps `main` linear.

---

## 🚀 Next Steps

* **Day 6–7:** integrate a **SQL DB** (JPA), migrate from in-memory store; add `@DataJpaTest` for repos.
* **Refactor (Day 7):** split `items` into `web/ dto/ domain/ service/ repo/` subpackages (today’s flat structure was **intentional** for speed).
* Consider adding `messages_es.properties` for Spanish (optional).
* Prepare CI (Day 11) to run tests on push.

---

## 📂 Deliverables

* **Code**

    * `src/main/java/com/ruben/backendportfolio/items/Item.java`
    * `src/main/java/com/ruben/backendportfolio/items/ItemCreateRequest.java`
    * `src/main/java/com/ruben/backendportfolio/items/ItemUpdateRequest.java`
    * `src/main/java/com/ruben/backendportfolio/items/ItemRepository.java`
    * `src/main/java/com/ruben/backendportfolio/items/InMemoryItemRepository.java`
    * `src/main/java/com/ruben/backendportfolio/items/ItemService.java`
    * `src/main/java/com/ruben/backendportfolio/items/ItemsController.java`
    * `src/main/java/com/ruben/backendportfolio/items/ItemNotFoundException.java`
    * `src/main/java/com/ruben/backendportfolio/items/NotFoundException.java`
    * `src/main/java/com/ruben/backendportfolio/errors/ApiError.java`
    * `src/main/java/com/ruben/backendportfolio/errors/GlobalExceptionHandler.java`
    * `src/main/java/com/ruben/backendportfolio/validation/ValidName.java`
    * `src/main/resources/messages.properties`

* **Tests**

    * `src/test/java/com/ruben/backendportfolio/items/ItemsControllerHappyPathTest.java`
    * `src/test/java/com/ruben/backendportfolio/items/ItemsControllerErrorTest.java`

* **Docs & tooling**

    * `docs/api.md` (updated)
    * `postman/collection.json` (updated)
    * `progress/day5-summary.md` (this file)

* **Cleanup**

    * Removed legacy `src/main/java/com/ruben/backendportfolio/api/ItemsController.java` and its test.
