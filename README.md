# 🚗 High-Performance Car Engine & Simulation – Multi-Threaded ECS Architecture

An advanced, lock-free, deterministic car simulation engine built with **Spring Boot (Backend)** and **React (Frontend)**.  
This project was constructed to solve distributed systems and concurrency challenges, utilizing modern game-engine architecture to guarantee high-speed tick-rates over REST/WebSockets without data races or thread deadlocks.

---

##  Core Architecture & Design

### Entity-Component-System (ECS) Purity
- **Data-Oriented Design:** Complete segregation of business logic, mathematical models, and UI mapping. `EntityId` acts as the pure identity, mapping dynamically to isolated components (`SpeedComponent`, `EngineComponent`, etc.).

### Double-layered Hybrid Push-Pull Observer Pattern
- **The Push Boundary (`PushObserver<T>`):** The core physics model securely *pushes* highly-generic, immutable snapshots directly across thread boundaries using captured lambdas. This mathematically eliminates `NullPointerExceptions` caused by concurrent entity mapping/garbage collection.
- **The Pull Boundary:** Composite UI views (like `GameStateView`) structurally *pull* from securely synchronized downstream caches to build unified DTO objects, preventing views from reaching blindly into rapidly mutating physics algorithms.

---

##  Concurrency & Lock-Free Threading

To achieve deterministic simulation speed, the system implements a strict **Three-Stage Thread Pipeline** (mirroring professional AAA game mechanics):

1. **Input Stage (REST / WebSockets)**
    - HTTP threads do not block or manually mutate data. User interactions are rapidly digested onto a non-blocking `CommandQueue` to guarantee zero server latency.
2. **Physics Stage (`GameLoopThread`)**
    - Freed from String concatenation and DTO mapping, the core logic thread securely drains the Command Queue and computes pure telemetry mathematics.
    - Constructed using **Optimistic Concurrency** and **Atomic Guards** instead of heavy, unscalable `synchronized` locks.
3. **Rendering Stage (`ObserverThread`)**
    - A dedicated rendering background thread cleanly empties an `ObserverDispatcher` queue, separating network serialization workload from the physics simulation.

### Torn-Read & Torn-Frame Prevention
- Engineered a **Batched-UI pipeline** that perfectly encapsulates varied asynchronous metric calculations into a single flawless mathematical frame. This structurally eliminates 'Torn Reads', 'Torn Frames', race-conditions, and 'Ghost/Zombie' entity anomalies from appearing on the frontend network socket. By orchestrating a final, unified push of a `GameStateDTO` to the WebSocket only at the end of the physics tick, it improves performance and guarantees that the frontend renders a complete, coherent frame (avoiding visual stuttering mid-calculation).

---

##  API & Communication

### REST API
- Provides endpoints for interacting with the simulation:
    - Update properties (speed, color, engine, etc.)
    - Retrieve current game state
- Built with **Spring Boot REST controllers**

### WebSocket (Real-Time Updates)
- Push-based communication for live updates to the frontend
- Sends updated `GameStateDTO` objects to clients instantly
- Eliminates the need for polling

Enables a **responsive real-time UI experience**

---

##  Simulation Features

- **Pathfinding using Dijkstra Algorithm**
- Graph-based movement system with interactive nodes
- Dynamic entity lifecycle:
    - Create / remove entities at runtime (only in backend yet)
- Real-time updates of:
    - Position
    - Speed
    - Engine type
    - Energy storage
    - Color
    - State & messages

---

##  Persistence Layer

- **SQLite + Spring Data JPA**
- Save and load full simulation state
- Snapshot-based persistence model

---

##  Frontend (Prompt Engineered with Antigravity)

- Built with **React**
- Consumes aggregated backend state (`GameStateDTO`)
- Displays **live simulation updates**

---

## Testing

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