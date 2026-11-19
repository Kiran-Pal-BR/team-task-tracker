---
sessionId: session-261117-141850-ddu0
---

# Postman Test Cases — Team Task Tracker API

**Base URL:** `http://localhost:8080`

**Note:** Replace `<USER_ID>` with any valid UUID (e.g., `550e8400-e29b-41d4-a716-446655440000`) for the `X-User-Id` header. After creating your first resources, use the actual IDs returned in responses for subsequent tests.

---

## 1. Projects API (`/api/projects`)

### 1.1 Create a Project (POST)
**Endpoint:** `POST /api/projects`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "name": "Team Task Tracker",
  "description": "A project to track team tasks"
}
```
**Expected:** `201 Created` — returns the created project with an `id`, `createdAt`, `createdBy`, and `updatedBy` populated.

### 1.2 Create Project — Missing X-User-Id Header
**Endpoint:** `POST /api/projects`
**Headers:** (none)
**Body (JSON):**
```json
{
  "name": "No Header Project",
  "description": "Should fail"
}
```
**Expected:** `400 Bad Request` — `{"error": "Missing mandatory 'X-User-Id' header"}`

### 1.3 Create Project — Blank Name (Validation)
**Endpoint:** `POST /api/projects`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "name": "   ",
  "description": "Blank name should fail"
}
```
**Expected:** `400 Bad Request` — validation error for `name` field ("Project name cannot be blank")

### 1.4 Create Project — Name Too Long (Validation)
**Endpoint:** `POST /api/projects`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "name": "A name that is way too long and exceeds the maximum allowed length of one hundred characters for the project name field in the database schema",
  "description": "Should fail"
}
```
**Expected:** `400 Bad Request` — validation error for `name` field ("Project name must not exceed 100 characters")

### 1.5 Get All Projects (GET)
**Endpoint:** `GET /api/projects`
**Expected:** `200 OK` — returns an array of all projects.

### 1.6 Search Projects by Name (GET)
**Endpoint:** `GET /api/projects?name=task`
**Expected:** `200 OK` — returns projects whose names contain "task" (case-insensitive partial match).

### 1.7 Search Projects — No Matches
**Endpoint:** `GET /api/projects?name=nonexistentprojectxyz`
**Expected:** `200 OK` — returns an empty array `[]`.

### 1.8 Get Project by ID (GET)
**Endpoint:** `GET /api/projects/<PROJECT_ID>`
**Expected:** `200 OK` — returns the single project.

### 1.9 Get Project by ID — Not Found
**Endpoint:** `GET /api/projects/00000000-0000-0000-0000-000000000000`
**Expected:** `404 Not Found` — `{"error": "Project not found with id: 00000000-0000-0000-0000-000000000000"}`

### 1.10 Get Project — Invalid UUID Format
**Endpoint:** `GET /api/projects/abc`
**Expected:** `400 Bad Request` — `{"error": "Parameter 'id' should be of type UUID"}`

### 1.11 Full Update Project — PUT
**Endpoint:** `PUT /api/projects/<PROJECT_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "name": "Updated Project Name",
  "description": "Fully updated description"
}
```
**Expected:** `200 OK` — returns the updated project with both fields replaced.

### 1.12 Full Update Project — PUT with Blank Name
**Endpoint:** `PUT /api/projects/<PROJECT_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "name": "",
  "description": "Name is blank"
}
```
**Expected:** `400 Bad Request` — `{"error": "Project name cannot be blank"}`

### 1.13 Partial Update Project — PATCH (Name Only)
**Endpoint:** `PATCH /api/projects/<PROJECT_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "name": "Patched Name"
}
```
**Expected:** `200 OK` — only `name` is updated; `description` remains unchanged.

### 1.14 Partial Update Project — PATCH (Description Only)
**Endpoint:** `PATCH /api/projects/<PROJECT_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "description": "Only description changed"
}
```
**Expected:** `200 OK` — only `description` is updated; `name` remains unchanged.

### 1.15 Partial Update Project — PATCH with Blank Name
**Endpoint:** `PATCH /api/projects/<PROJECT_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "name": "   "
}
```
**Expected:** `400 Bad Request` — blank name is rejected in PATCH too.

### 1.16 Delete Project (DELETE)
**Endpoint:** `DELETE /api/projects/<PROJECT_ID>`
**Expected:** `204 No Content` — no response body.

### 1.17 Delete Project — Not Found
**Endpoint:** `DELETE /api/projects/00000000-0000-0000-0000-000000000000`
**Expected:** `404 Not Found` — `{"error": "Project not found with id: 00000000-0000-0000-0000-000000000000"}`

### 1.18 Delete Project — Already Deleted
**Endpoint:** `DELETE /api/projects/<DELETED_PROJECT_ID>`
**Expected:** `404 Not Found` — project was already removed.

### 1.19 Create Project — Malformed JSON Body
**Endpoint:** `POST /api/projects`
**Headers:** `X-User-Id: <USER_ID>`, `Content-Type: application/json`
**Body:** `{invalid json}`
**Expected:** `400 Bad Request` — `{"error": "Malformed JSON request body or invalid field value"}`

---

## 2. Users API (`/api/users`)

### 2.1 Create a User (POST)
**Endpoint:** `POST /api/users`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "keycloakId": "kc-abc-123"
}
```
**Expected:** `201 Created` — returns the created user with `id`, `createdAt`, `createdBy`, and `updatedBy` populated.

### 2.2 Create User — Missing X-User-Id Header
**Endpoint:** `POST /api/users`
**Headers:** (none)
**Body (JSON):**
```json
{
  "username": "noheader",
  "email": "noheader@example.com",
  "keycloakId": "kc-noheader"
}
```
**Expected:** `400 Bad Request` — `{"error": "Missing mandatory 'X-User-Id' header"}`

### 2.3 Create User — Duplicate Username
**Endpoint:** `POST /api/users`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "username": "johndoe",
  "email": "different@example.com",
  "keycloakId": "kc-different"
}
```
**Expected:** `400 Bad Request` — `{"error": "Username already exists: johndoe"}`

### 2.4 Create User — Duplicate Email
**Endpoint:** `POST /api/users`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "username": "differentuser",
  "email": "john@example.com",
  "keycloakId": "kc-different"
}
```
**Expected:** `400 Bad Request` — `{"error": "Email already exists: john@example.com"}`

### 2.5 Create User — Duplicate Keycloak ID
**Endpoint:** `POST /api/users`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "username": "anotheruser",
  "email": "another@example.com",
  "keycloakId": "kc-abc-123"
}
```
**Expected:** `400 Bad Request` — `{"error": "Keycloak ID already exists: kc-abc-123"}`

### 2.6 Get All Users (GET)
**Endpoint:** `GET /api/users`
**Expected:** `200 OK` — returns an array of all users.

### 2.7 Get User by Email (GET)
**Endpoint:** `GET /api/users?email=john@example.com`
**Expected:** `200 OK` — returns a single-element array with the matching user (case-insensitive).

### 2.8 Get User by Email — Case Insensitive
**Endpoint:** `GET /api/users?email=JOHN@EXAMPLE.COM`
**Expected:** `200 OK` — returns the same user as test 2.7.

### 2.9 Get User by Keycloak ID (GET)
**Endpoint:** `GET /api/users?keycloakId=kc-abc-123`
**Expected:** `200 OK` — returns a single-element array with the matching user.

### 2.10 Get User by ID (GET)
**Endpoint:** `GET /api/users/<USER_RESOURCE_ID>`
**Expected:** `200 OK` — returns the single user.

### 2.11 Get User by ID — Not Found
**Endpoint:** `GET /api/users/00000000-0000-0000-0000-000000000000`
**Expected:** `404 Not Found` — `{"error": "User not found with id: 00000000-0000-0000-0000-000000000000"}`

### 2.12 Full Update User — PUT
**Endpoint:** `PUT /api/users/<USER_RESOURCE_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "username": "johndoe_updated",
  "email": "john.updated@example.com",
  "keycloakId": "kc-abc-123-updated"
}
```
**Expected:** `200 OK` — returns the updated user with all fields replaced.

### 2.13 Full Update User — PUT with Duplicate Email (Conflict)
**Endpoint:** `PUT /api/users/<USER_RESOURCE_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "username": "conflict",
  "email": "another_existing_user_email",
  "keycloakId": "kc-new"
}
```
(Use an email that belongs to a different user)
**Expected:** `400 Bad Request` — `{"error": "Email already exists: ..."}`

### 2.14 Partial Update User — PATCH (Username Only)
**Endpoint:** `PATCH /api/users/<USER_RESOURCE_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "username": "patched_username"
}
```
**Expected:** `200 OK` — only `username` is updated; `email` and `keycloakId` remain unchanged.

### 2.15 Partial Update User — PATCH with Blank Username
**Endpoint:** `PATCH /api/users/<USER_RESOURCE_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "username": "   "
}
```
**Expected:** `400 Bad Request` — blank username is rejected.

### 2.16 Delete User (DELETE)
**Endpoint:** `DELETE /api/users/<USER_RESOURCE_ID>`
**Expected:** `204 No Content` — no response body.

### 2.17 Delete User — Not Found
**Endpoint:** `DELETE /api/users/00000000-0000-0000-0000-000000000000`
**Expected:** `404 Not Found` — `{"error": "User not found with id: 00000000-0000-0000-0000-000000000000"}`

---

## 3. Tasks API (`/api/tasks`)

**Prerequisites:** Create a Project and a User first (from sections 1 and 2 above) to use their IDs.

### 3.1 Create a Task (POST)
**Endpoint:** `POST /api/tasks`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "projectId": "<PROJECT_ID>",
  "assignedTo": "<USER_RESOURCE_ID>",
  "title": "Implement login feature",
  "description": "Add OAuth2 login with Keycloak",
  "status": "TODO",
  "priority": "HIGH",
  "dueDate": "2025-12-31T23:59:59"
}
```
**Expected:** `201 Created` — returns the created task with `id`, `createdAt`, `createdBy`, and `updatedBy` populated.

### 3.2 Create Task — Missing X-User-Id Header
**Endpoint:** `POST /api/tasks`
**Headers:** (none)
**Body (JSON):**
```json
{
  "projectId": "<PROJECT_ID>",
  "title": "No header task"
}
```
**Expected:** `400 Bad Request` — `{"error": "Missing mandatory 'X-User-Id' header"}`

### 3.3 Create Task — Blank Title
**Endpoint:** `POST /api/tasks`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "projectId": "<PROJECT_ID>",
  "title": "   "
}
```
**Expected:** `400 Bad Request` — `{"error": "Task title cannot be blank"}`

### 3.4 Create Task — Invalid Project ID
**Endpoint:** `POST /api/tasks`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "projectId": "00000000-0000-0000-0000-000000000000",
  "title": "Orphan task"
}
```
**Expected:** `404 Not Found` — `{"error": "Project not found with id: 00000000-0000-0000-0000-000000000000"}`

### 3.5 Create Task — Invalid AssignedTo User ID
**Endpoint:** `POST /api/tasks`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "projectId": "<PROJECT_ID>",
  "assignedTo": "00000000-0000-0000-0000-000000000000",
  "title": "Orphan assignee"
}
```
**Expected:** `404 Not Found` — `{"error": "User not found with id: 00000000-0000-0000-0000-000000000000"}`

### 3.6 Create Task — Defaults Applied (No Status/Priority)
**Endpoint:** `POST /api/tasks`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "projectId": "<PROJECT_ID>",
  "title": "Default status task"
}
```
**Expected:** `201 Created` — `status` should be `TODO` and `priority` should be `MEDIUM` in the response.

### 3.7 Get All Tasks (GET)
**Endpoint:** `GET /api/tasks`
**Expected:** `200 OK` — returns an array of all tasks.

### 3.8 Get Tasks Filtered by Project ID (GET)
**Endpoint:** `GET /api/tasks?projectId=<PROJECT_ID>`
**Expected:** `200 OK` — returns only tasks belonging to the specified project.

### 3.9 Get Tasks Filtered by AssignedTo (GET)
**Endpoint:** `GET /api/tasks?assignedTo=<USER_RESOURCE_ID>`
**Expected:** `200 OK` — returns only tasks assigned to the specified user.

### 3.10 Get Tasks Filtered by Status (GET)
**Endpoint:** `GET /api/tasks?status=TODO`
**Expected:** `200 OK` — returns only tasks with status `TODO`.

### 3.11 Get Tasks — Combined Filter (Project + Status)
**Endpoint:** `GET /api/tasks?projectId=<PROJECT_ID>&status=IN_PROGRESS`
**Expected:** `200 OK` — returns tasks matching both the project and status.

### 3.12 Get Tasks — No Matches
**Endpoint:** `GET /api/tasks?status=COMPLETED`
**Expected:** `200 OK` — returns an empty array `[]` if no tasks are completed.

### 3.13 Get Task by ID (GET)
**Endpoint:** `GET /api/tasks/<TASK_ID>`
**Expected:** `200 OK` — returns the single task.

### 3.14 Get Task by ID — Not Found
**Endpoint:** `GET /api/tasks/00000000-0000-0000-0000-000000000000`
**Expected:** `404 Not Found` — `{"error": "Task not found with id: 00000000-0000-0000-0000-000000000000"}`

### 3.15 Full Update Task — PUT
**Endpoint:** `PUT /api/tasks/<TASK_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "projectId": "<PROJECT_ID>",
  "assignedTo": "<USER_RESOURCE_ID>",
  "title": "Updated task title",
  "description": "Fully updated",
  "status": "IN_PROGRESS",
  "priority": "URGENT",
  "dueDate": "2025-11-01T18:00:00"
}
```
**Expected:** `200 OK` — returns the task with all fields replaced.

### 3.16 Full Update Task — PUT with Invalid Project
**Endpoint:** `PUT /api/tasks/<TASK_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "projectId": "00000000-0000-0000-0000-000000000000",
  "title": "Bad project ref",
  "status": "TODO",
  "priority": "LOW"
}
```
**Expected:** `404 Not Found` — `{"error": "Project not found with id: 00000000-0000-0000-0000-000000000000"}`

### 3.17 Partial Update Task — PATCH (Status Only)
**Endpoint:** `PATCH /api/tasks/<TASK_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "status": "COMPLETED"
}
```
**Expected:** `200 OK` — only `status` is updated; all other fields remain unchanged.

### 3.18 Partial Update Task — PATCH (Priority Only)
**Endpoint:** `PATCH /api/tasks/<TASK_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "priority": "LOW"
}
```
**Expected:** `200 OK` — only `priority` is updated.

### 3.19 Partial Update Task — PATCH (Title Only)
**Endpoint:** `PATCH /api/tasks/<TASK_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "title": "Patched title"
}
```
**Expected:** `200 OK` — only `title` is updated.

### 3.20 Partial Update Task — PATCH with Blank Title
**Endpoint:** `PATCH /api/tasks/<TASK_ID>`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "title": ""
}
```
**Expected:** `400 Bad Request` — blank title is rejected.

### 3.21 Delete Task (DELETE)
**Endpoint:** `DELETE /api/tasks/<TASK_ID>`
**Expected:** `204 No Content` — no response body.

### 3.22 Delete Task — Not Found
**Endpoint:** `DELETE /api/tasks/00000000-0000-0000-0000-000000000000`
**Expected:** `404 Not Found` — `{"error": "Task not found with id: 00000000-0000-0000-0000-000000000000"}`

---

## 4. Project Members API (`/api/project-members`)

**Prerequisites:** Create a Project and a User first.

### 4.1 Add Member to Project (POST)
**Endpoint:** `POST /api/project-members?projectId=<PROJECT_ID>&userId=<USER_RESOURCE_ID>`
**Expected:** `201 Created` — returns the `ProjectMember` with `joinedAt` populated.

### 4.2 Add Member — Duplicate (Same User to Same Project)
**Endpoint:** `POST /api/project-members?projectId=<PROJECT_ID>&userId=<USER_RESOURCE_ID>`
(Same IDs as test 4.1)
**Expected:** `400 Bad Request` — `{"error": "User is already a member of this project"}`

### 4.3 Add Member — Project Not Found
**Endpoint:** `POST /api/project-members?projectId=00000000-0000-0000-0000-000000000000&userId=<USER_RESOURCE_ID>`
**Expected:** `404 Not Found` — `{"error": "Project not found with id: 00000000-0000-0000-0000-000000000000"}`

### 4.4 Add Member — User Not Found
**Endpoint:** `POST /api/project-members?projectId=<PROJECT_ID>&userId=00000000-0000-0000-0000-000000000000`
**Expected:** `404 Not Found` — `{"error": "User not found with id: 00000000-0000-0000-0000-000000000000"}`

### 4.5 Get Members of a Project (GET)
**Endpoint:** `GET /api/project-members?projectId=<PROJECT_ID>`
**Expected:** `200 OK` — returns an array of `ProjectMember` entries for the project.

### 4.6 Get Projects of a User (GET)
**Endpoint:** `GET /api/project-members?userId=<USER_RESOURCE_ID>`
**Expected:** `200 OK` — returns an array of `ProjectMember` entries for the user.

### 4.7 Get Memberships — Missing Both Parameters
**Endpoint:** `GET /api/project-members`
**Expected:** `400 Bad Request` — `{"error": "Either 'projectId' or 'userId' query parameter must be provided"}`

### 4.8 Remove Member from Project (DELETE)
**Endpoint:** `DELETE /api/project-members?projectId=<PROJECT_ID>&userId=<USER_RESOURCE_ID>`
**Expected:** `204 No Content` — no response body.

### 4.9 Remove Member — Not Found (Already Removed)
**Endpoint:** `DELETE /api/project-members?projectId=<PROJECT_ID>&userId=<USER_RESOURCE_ID>`
(Same IDs as test 4.8)
**Expected:** `404 Not Found` — `{"error": "Membership not found for user in project"}`

### 4.10 Add Member — Invalid UUID Format
**Endpoint:** `POST /api/project-members?projectId=abc&userId=<USER_RESOURCE_ID>`
**Expected:** `400 Bad Request` — `{"error": "Parameter 'projectId' should be of type UUID"}`

---

## 5. Global Exception Handler Tests

### 5.1 Malformed JSON (Any POST/PUT Endpoint)
**Endpoint:** `POST /api/projects`
**Headers:** `X-User-Id: <USER_ID>`, `Content-Type: application/json`
**Body:** `{this is not valid json}`
**Expected:** `400 Bad Request` — `{"error": "Malformed JSON request body or invalid field value"}`

### 5.2 Invalid UUID in Path Variable
**Endpoint:** `GET /api/projects/not-a-uuid`
**Expected:** `400 Bad Request` — `{"error": "Parameter 'id' should be of type UUID"}`

### 5.3 Bean Validation Error (Blank Project Name via @Valid)
**Endpoint:** `POST /api/projects`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "name": "",
  "description": "Validation test"
}
```
**Expected:** `400 Bad Request` — returns field-level errors map with `name` key.

### 5.4 Database Constraint Violation (Duplicate Entry)
**Endpoint:** `POST /api/users`
**Headers:** `X-User-Id: <USER_ID>`
**Body (JSON):**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "keycloakId": "kc-abc-123"
}
```
(Same data as an already created user)
**Expected:** `400 Bad Request` — uniqueness check in service returns `BadRequestException` before reaching the database.

---

## Summary

| Module | Endpoints | Test Cases |
|---|---|---|
| **Projects** | POST, GET (all/search), GET (by ID), PUT, PATCH, DELETE | 19 |
| **Users** | POST, GET (all/filter), GET (by ID), PUT, PATCH, DELETE | 17 |
| **Tasks** | POST, GET (all/filter), GET (by ID), PUT, PATCH, DELETE | 22 |
| **Project Members** | POST, GET, DELETE | 10 |
| **Global Exceptions** | Malformed JSON, invalid UUID, validation, constraints | 4 |
| **Total** | **24 endpoints** | **72 test cases** |

# Delivery Steps

### Step 1: Test Projects and Users APIs
Execute all test cases for Projects (1.1–1.19) and Users (2.1–2.17) to validate CRUD operations, validation, uniqueness checks, and error handling.

### Step 2: Test Tasks and Project Members APIs
Execute all test cases for Tasks (3.1–3.22) and Project Members (4.1–4.10) including reference validation, filtering, defaults, and the global exception handler tests (5.1–5.4).