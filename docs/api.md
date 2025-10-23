# API — backend-portfolio

**Purpose:** Minimal, deterministic endpoints that form the baseline for future CRUD and persistence work.

## Base URL

* Local: `http://localhost:8080`

## How to Run (local)

```bash
mvn -q spring-boot:run
```

## Endpoints

### GET /health

**Description:** Service liveness check (useful for Docker/CI).

**cURL**

```bash
curl -s http://localhost:8080/health
```

**Expected 200 Response**

```json
{ "status": "ok", "service": "backend-portfolio" }
```

---

### GET /items

**Description:** Static list to validate JSON shape and serialization.

**cURL**

```bash
curl -s http://localhost:8080/items
```

**Expected 200 Response**

```json
[
  { "id": 1, "name": "alpha" },
  { "id": 2, "name": "beta" }
]
```

---

## Postman

* Environment: `postman/environment.json` (sets `baseUrl`)
* Collection: `postman/collection.json` (requests for `/health` and `/items`)
* Usage:

    1. Import both files into Postman
    2. Select environment **backend-portfolio-local**
    3. Send requests and compare with the expected responses above

## Notes

* Responses are **deterministic** (no timestamps/random) to keep docs/tests stable.
* Change the port via `src/main/resources/application.properties` (e.g., `server.port=8081`) if needed.