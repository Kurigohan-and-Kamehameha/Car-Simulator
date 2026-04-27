# 🚗 High-Performance Car Entity Simulation

A lock-free, deterministic car simulation engine designed to solve distributed systems and concurrency challenges. Built with **Spring Boot** and **React**.

## Tech Stack
- **Backend:** Java 21, Spring Boot, Spring Data JPA, SQLite, WebSockets/STOMP
- **Frontend:** React, Vite
- **DevOps & Deployment:** Docker, Docker Compose, GitHub Actions (CI/CD)

> **Note on Frontend:** As my core engineering focus for this project lies in backend architecture and concurrent systems, the React Frontend was built using prompt engineering.

---

## Running the Application

### Option 1: Docker (Recommended)
You can start the entire application (Frontend + Backend) with a single command!
Ensure Docker is installed, then run in the project root:
```bash
docker-compose up -d --build
```
- The Web UI will be available at: `http://localhost:80`
- The Backend REST API at: `http://localhost:8080`

### Option 2: Local Development (Manual Start)
**Backend:**
1. Navigate to `backend/`
2. Run `.\mvnw spring-boot:run` (Windows) or `./mvnw spring-boot:run`
3. Server starts on `http://localhost:8080`

**Frontend:**
1. Navigate to `frontend/`
2. Run `npm install` (first time only) then `npm run dev`
3. UI starts on `http://localhost:5173`

---

## Simulation Features
- **Pathfinding via Dijkstra Algorithm** on an interactive graph-based movement system.
- **Dynamic Entity Lifecycle:** Create and remove vehicles at runtime.  (only backend implementation yet)
- **Real-Time Telemetry:** Live updates for Position, Speed, Engine type, Energy storage, and Color pushed instantly via WebSockets.

## Persistence
- **SQLite + Spring Data JPA**
- Save and load full simulation state.
- Snapshot-based persistence model.

## Testing
- Unit tests focusing on:
    - Core logic (e.g., pathfinding, command execution)
    - Observer system behavior
    - Snapshot correctness
- Focus on deterministic logic validation and edge cases (e.g., concurrent updates).

---

## Deep Dive: Architecture & Concurrency

*(This section is for technical reviewers and engineers interested in the engine's core mechanics)*

### Entity-Component-System (ECS) Purity
- **Data-Oriented Design:** Complete segregation of business logic, mathematical models, and UI mapping. `EntityId` acts as the pure identity, mapping dynamically to isolated components (`SpeedComponent`, `EngineComponent`, etc.).

### Lock-Free Threading Pipeline
Achieves deterministic simulation speed using a strict **Three-Stage Thread Pipeline** to prevent data races and deadlocks:
1. **Input Stage (REST / WebSockets):** HTTP threads do not block or mutate data. User interactions are rapidly placed onto a non-blocking `CommandQueue` guaranteeing zero server latency.
2. **Physics Stage (`GameLoopThread`):** The core logic thread securely drains the Command Queue and computes pure telemetry mathematics using **Optimistic Concurrency** and **Atomic Guards** instead of heavy `synchronized` locks.
3. **Rendering Stage (`ObserverThread`):** A dedicated background thread drains an `ObserverDispatcher` queue, completely separating network serialization workloads from the physics simulation.

### Double-layered Hybrid Push-Pull Observer Pattern
- **The Push Boundary (`PushObserver<T>`):** The core physics model securely *pushes* highly-generic, immutable snapshots directly across thread boundaries using captured lambdas. This mathematically eliminates `NullPointerExceptions` caused by concurrent entity mapping/garbage collection.
- **The Pull Boundary:** Composite UI views (like `GameStateView`) structurally *pull* from securely synchronized downstream caches to build unified DTO objects, preventing views from reaching blindly into rapidly mutating physics algorithms.

### Torn-Read & Torn-Frame Prevention
- **Batched-UI pipeline:** Engineered a Batched-UI pipeline that perfectly encapsulates varied asynchronous metric calculations into a single flawless mathematical frame. This structurally eliminates 'Torn Reads', 'Torn Frames', race-conditions, and 'Ghost/Zombie' entity anomalies from appearing on the frontend network socket. By orchestrating a final, unified push of a `GameStateDTO` to the WebSocket only at the end of the physics tick, it improves performance and guarantees that the frontend renders a complete, coherent frame (avoiding visual stuttering mid-calculation).