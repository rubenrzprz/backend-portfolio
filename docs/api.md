# Backend Portfolio — API Guide

This document explains how to run the API, the available endpoints, expected request/response formats, and common error cases.

---

## Running the Stack (Docker)

There are two common dev modes:

### A) Default — API + DB (containerized)

Brings up both services so the API is reachable at `http://localhost:8080`.

```bash
docker compose up -d --build
curl http://localhost:8080/health
```

### B) DB-only (for running the app **locally**)

Starts only Postgres so you can run Spring Boot via Maven on your machine.

```bash
# Start only the database container
docker compose up -d db

# In another terminal (local app)
mvn -q -D spring-boot.run.profiles=local spring-boot:run
```

> If the app container is already running and you need to free port 8080:
>
> ```bash
> docker compose stop app
> docker compose rm -f app
> ```

**Notes**

* The datasource is **env-driven**:

    * In Docker, the app uses the `docker` profile (DB host `db`).
    * Locally, defaults point to `localhost:5432` (`bp_user/bp_pass/bp_db`) unless you override via env.
* You can fully override with `SPRING_DATASOURCE_URL`, or compose it via `POSTGRES_HOST/PORT/DB`, `POSTGRES_USER/PASSWORD`.

---

## Base URL

```
http://localhost:8080
```

---

## Health

### `GET /health`

**Response (200)**

```json
{
  "status": "ok",
  "service": "backend-portfolio"
}
```

---

## Items

### Model (server response shape)

```json
{
  "id": 1,
  "name": "Example item"
}
```

### Create

`POST /items`

**Request**

```json
{
  "name": "New item"
}
```

Rules:

* `name`: required, 2–40 chars.

**Responses**

* **201 Created**
  Headers: `Location: /items/{id}`
  Body:

  ```json
  { "id": 123, "name": "New item" }
  ```

* **400 Bad Request** (validation):

  ```json
  {
    "error": "validation_error",
    "details": [
      {"field": "name", "message": "name is required"},
      {"field": "name", "message": "name must be 2-40 chars"}
    ]
  }
  ```

### Get (all)

`GET /items`

**Response (200)**

```json
[
  {"id": 1, "name": "Alpha"},
  {"id": 2, "name": "Beta"}
]
```

### Get (by id)

`GET /items/{id}`

**Responses**

* **200 OK**

  ```json
  { "id": 1, "name": "Alpha" }
  ```
* **404 Not Found**

  ```json
  { "error": "not_found", "message": "Item not found" }
  ```

### Update (replace)

`PUT /items/{id}`

**Request**

```json
{ "name": "Updated name" }
```

**Responses**

* **200 OK**

  ```json
  { "id": 1, "name": "Updated name" }
  ```
* **400 Bad Request** (validation as in POST)
* **404 Not Found**

### Update (partial)

`PATCH /items/{id}`

**Request**

```json
{ "name": "Patched name" }
```

**Responses**

* **200 OK**

  ```json
  { "id": 1, "name": "Patched name" }
  ```
* **400 Bad Request** (validation as in POST)
* **404 Not Found**

### Delete

`DELETE /items/{id}`

**Responses**

* **204 No Content**
* **404 Not Found**

---

## Error Semantics (summary)

* **400** — validation errors (missing/invalid fields).
* **404** — resource not found.
* **500** — unexpected server errors.

Example error envelope (typical):

```json
{
  "error": "validation_error",
  "details": [
    {"field": "name", "message": "name is required"}
  ]
}
```

---

## Postman

**Environment:** `postman/environment.json` (sets `baseUrl`)
**Collection:** `postman/collection.json` (requests for `/health` and `/items` with tests and id-chaining)

### What’s in the collection

* `Health` checks **200** and `status="ok"`.
* `Items — Create` asserts **201**, captures the returned `id`, verifies the `Location` header includes `/items/`, and **stores** `itemId` in the environment.
* `Items — List` asserts **200** and that the body is an array.
* `Items — Get by id` uses `{{itemId}}` and checks the response id matches the stored value.
* `Items — Put` and `Items — Patch` assert **200** and verify the updated name (`alpha-2`, `alpha-3`).
* `Items — Delete` asserts **204**.
* Error cases: invalid create → **400** with `validation_error`; unknown id → **404** with `not_found`.

### Usage

1. **Import** both files into Postman:

    * `postman/environment.json`
    * `postman/collection.json`
2. Select environment: **`backend-portfolio-local`** (or your environment name).

    * Ensure `baseUrl = http://localhost:8080` (works for both containerized and local runs).
3. Run the requests **in order**:

    1. Health
    2. Items — Create
    3. Items — List
    4. Items — Get by id
    5. Items — Put
    6. Items — Patch
    7. Items — Delete
    8. Errors — Create invalid (400)
    9. Errors — Get unknown id (404)
4. Check the **Tests** tab in each request for pass/fail assertions.

> Tip: Use the **Collection Runner** to execute the full flow in one go. The collection relies on the `itemId` variable created by the **Create** step.

---

## Quick cURL Examples

Create:

```bash
curl -s -X POST http://localhost:8080/items \
  -H "Content-Type: application/json" \
  -d '{"name":"Notebook"}'
```

List:

```bash
curl -s http://localhost:8080/items
```

Get by id:

```bash
curl -s http://localhost:8080/items/1
```

Update:

```bash
curl -s -X PUT http://localhost:8080/items/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated"}'
```

Patch:

```bash
curl -s -X PATCH http://localhost:8080/items/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Patched"}'
```

Delete:

```bash
curl -i -X DELETE http://localhost:8080/items/1
```

---

## Troubleshooting

* **`/items` returns 500 after a fresh setup**
  Use auto-init or run `schema.sql`/`seed.sql` manually (see `docs/sql.md`), then retry.

* **Port 8080 already in use**
  Stop a previous app container:

  ```bash
  docker compose stop app
  docker compose rm -f app
  ```

* **Postman tests fail on `itemId`**
  Ensure you ran **Items — Create** first; it sets `itemId` in the environment.
