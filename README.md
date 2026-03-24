# Car Game Multi-Project

This project is divided into two independent modules:

- **`backend/`**: Spring Boot application (Maven)
- **`frontend/`**: React application (Vite)

## IDE Setup (VS Code / IntelliJ)

To avoid classpath errors and "package mismatch" warnings:
**Do NOT open this root folder in your IDE.** Instead:

1.  **Open the `/backend` folder** directly in a new window.
2.  **Open the `/frontend` folder** directly in another window.

This ensures each project's root and configuration files (like `pom.xml`) are correctly recognized.

## Running the Application

### Backend
1.  Navigate to `backend/`.
2.  Run `.\mvnw spring-boot:run` (Windows) or `./mvnw spring-boot:run` (Linux/Mac).
3.  The server starts on `http://localhost:8080`.

### Frontend
1.  Navigate to `frontend/`.
2.  Run `npm install` (first time only) then `npm run dev`.
3.  The UI starts on `http://localhost:5173` (or similar).
