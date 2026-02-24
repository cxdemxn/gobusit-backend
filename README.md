# GoBusIt Backend

The GoBusIt backend powers the core transport system for the platform.

It provides APIs for managing buses, routes, schedules, ticket booking, and real‑time vehicle tracking.  
The backend also contains the simulation engine used during development before real GPS devices are integrated.

---

## 🚀 Tech Stack

- Java + Spring Boot
- PostgreSQL
- Spring Data JPA
- WebSocket (for live bus updates)
- REST API

---

## 📦 Features

### Core Transport Logic
- Bus management
- Route creation and editing
- Trip scheduling
- Seat booking system

### Real‑Time Simulation
- Buses move along route geometry
- Position updates streamed to clients
- Can later switch to real GPS feeds

### User Operations
- Ticket booking
- Schedule browsing
- Trip lookup

---

## 📂 Project Structure
/src
|--/controllers
|--/services
|--/repositories
|--/entities
|--/simulation


---

## ⚙️ Running Locally

1. Clone the repository
2. Create a PostgreSQL database
3. Update `application.yml` with your DB credentials
4. Run the application using Gradle:
If using the Gradle wrapper (recommended): ./gradlew bootRun
On Windows: gradlew.bat bootRun


Server starts on: http://localhost:8080

---

## 🔗 Related Repositories

- gobusit-frontend → UI client at https://github.com/cxdemxn/gobusit-frontend
- gobusit-db → schema, migrations, and ERD at https://github.com/cxdemxn/gobusit-db

---

## 📌 Status

Backend currently in MVP development phase.
