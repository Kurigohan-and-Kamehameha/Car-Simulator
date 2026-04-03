# 🚗 Car Simulation Engine – Real-Time MVC/ECS Architecture - Double-layered Hybrid Push-Pull Observer Pattern

A modular, event-driven car simulation system built with **Spring Boot (backend)** and **React (frontend)**.  
This project focuses on **real-time updates, concurrency, and scalable architecture design**, going beyond typical CRUD applications.

---

## 🧠 Architecture & Design

### MVC + ECS
- Clear separation of concerns using **Model-View-Controller**
- Entity-based design (ECS-inspired) for flexible and scalable object management

### Event-Driven System (Observer Pattern)
- Decoupled communication between components
- Automatic propagation of state changes across the system
- Views react to model updates without tight coupling

### Snapshot Pattern
- Immutable DTOs (`GameStateDTO`, `SpeedSnapshot`, etc.)
- Ensures **thread-safe data sharing**
- Prevents inconsistent reads in concurrent environments

---

## ⚡ Concurrency & Real-Time Processing

- **Command Queue**
    - Processes game logic sequentially
    - Avoids race conditions in state mutations

- **Observer Dispatcher**
    - Synchronizes updates across views
    - Ensures consistent state aggregation

- Thread-safe data structures:
    - `ConcurrentHashMap`
    - `CopyOnWriteArrayList`

➡️ Designed for **live updates** instead of tick-based polling.

---

## 🌐 API & Communication

### REST API
- Provides endpoints for interacting with the simulation:
    - Update properties (speed, color, engine, etc.)
    - Retrieve current game state
- Built with **Spring Boot REST controllers**

### WebSocket (Real-Time Updates)
- Push-based communication for live updates to the frontend
- Sends updated `GameStateDTO` objects to clients instantly
- Eliminates the need for polling

➡️ Enables a **responsive real-time UI experience**

---

## 🗺️ Simulation Features

- **Pathfinding using Dijkstra Algorithm**
- Graph-based movement system with interactive nodes
- Dynamic entity lifecycle:
    - Create / remove entities at runtime (only in backend yet)
- Real-time updates of:
    - Position
    - Speed
    - Engine type
    - Energy storage
    - State & messages

---

## 💾 Persistence Layer

- **SQLite + Spring Data JPA**
- Save and load full simulation state
- Snapshot-based persistence model

---

## 🖥️ Frontend (Prompt Engineered with Antigravity)

- Built with **React**
- Consumes aggregated backend state (`GameStateDTO`)
- Displays **live simulation updates**

---

## 🧪 Testing

- Unit tests for:
    - Core logic (e.g. pathfinding, command execution)
    - Observer system behavior
    - Snapshot correctness

- Focus on:
    - Deterministic logic validation
    - Edge cases (e.g. entity removal, concurrent updates)

---

## Running the Application
This project is divided into two independent modules:

- **`backend/`**: Spring Boot application (Maven)
- **`frontend/`**: React application (Vite)

### Backend
1.  Navigate to `backend/`.
2.  Run `.\mvnw spring-boot:run` (Windows) or `./mvnw spring-boot:run` (Linux/Mac).
3.  The server starts on `http://localhost:8080`.

### Frontend
1.  Navigate to `frontend/`.
2.  Run `npm install` (first time only) then `npm run dev`.
3.  The UI starts on `http://localhost:5173` (or similar).
