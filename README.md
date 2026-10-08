# Notes Microservices

A small full-stack notes application built with Spring Boot, split into two independent services that talk to each other over REST.

## Architecture

| Service | Port | Purpose | Database |
|---|---|---|---|
| **Notes-app** | 8080 | Notes CRUD API and the web frontend | H2 (file: `notedb`) |
| **User-Service** | 8081 | User registration and login | H2 (file: `userdb`) |

When a note is created, the Notes app calls the User Service (`GET /users/{username}/exists`) to confirm the owner exists. If the User Service is down, the Notes app returns `503 Service Unavailable`. If the user does not exist, it returns `400 Bad Request`.

## Tech Stack

- Java, Spring Boot (Spring Web, Spring Data JPA)
- H2 file-based database
- BCrypt password hashing (`spring-security-crypto`)
- Vanilla HTML, CSS and JavaScript frontend
- Maven

## Features

- Create, read, update and delete notes
- Categories and last-updated timestamps
- Per-user notes: each user only sees their own
- Register and login with hashed passwords
- Service-to-service validation with graceful error handling

## API Overview

**Notes-app (port 8080)**

| Method | Path | Description |
|---|---|---|
| POST | `/Note` | Create a note (owner is validated against the User Service) |
| GET | `/Notes` | Get all notes |
| GET | `/notes/{id}` | Get one note |
| PUT | `/notes/{id}` | Update title, content and category |
| DELETE | `/notes/{id}` | Delete a note |
| GET | `/Notes/owner/{owner}` | Get notes for one user |

**User-Service (port 8081)**

| Method | Path | Description |
|---|---|---|
| POST | `/Register` | Create a user (password is hashed with BCrypt) |
| POST | `/Login` | Verify credentials |
| GET | `/users/{username}/exists` | Check whether a user exists |

## Running Locally

1. Start **User-Service** (port 8081).
2. Start **Notes-app** (port 8080).
3. Open `http://localhost:8080/login.html`, register a user, then log in.

Both services create their H2 database files automatically on first run.

## Project Structure

```
Notes-Microservices/
  Notes-app/       Notes API + frontend (static/login.html, static/index.html)
  User-Service/    User registration and login API
```

## Roadmap

- JWT-based authentication (login currently stores the username in the browser only)
- Pin / favorite notes
- Consistent endpoint naming (`/Note` vs `/notes`)
