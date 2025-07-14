# Affordmed URL Shortener

A full-stack URL shortener application with analytics, built using Spring Boot (Java), MongoDB, and React (Vite, MUI).

## Features
- Shorten long URLs with optional custom shortcodes and expiry
- View a list of all shortened URLs
- Track analytics: total clicks, click details (timestamp, referrer, geo info)
- Responsive, modern UI

## Tech Stack
- **Backend:** Java 17, Spring Boot, MongoDB
- **Frontend:** React, Vite, Material-UI (MUI), Axios

## Directory Structure
```
Affordmed/
  backend/affordmed-url-shortener/   # Spring Boot backend
  frontend/affordmed-url-shortener/  # React frontend
```

## Backend Setup
1. **Requirements:**
   - Java 17+
   - Maven
   - MongoDB (running locally on default port)

2. **Configuration:**
   - Default MongoDB URI: `mongodb://localhost:27017/urlshortener`
   - Server port: `8080`
   - Config file: `backend/affordmed-url-shortener/src/main/resources/application.properties`

3. **Run Backend:**
   ```sh
   cd backend/affordmed-url-shortener
   ./mvnw spring-boot:run
   # or
   mvn spring-boot:run
   ```

## Frontend Setup
1. **Requirements:**
   - Node.js (v18+ recommended)
   - npm

2. **Install & Run:**
   ```sh
   cd frontend/affordmed-url-shortener
   npm install
   npm run dev
   ```
   - The app runs on [http://localhost:5173](http://localhost:5173)

## Usage
- **Shorten URLs:** Enter a long URL, optional validity (in minutes), and optional custom shortcode. Click "Shorten".
- **View Shortened URLs:** See the list with copy and expiry info.
- **View Analytics:** Enter a shortcode in the analytics section to see click stats and details.

## API Endpoints (Backend)
- `POST /shorturls` — Create a short URL
- `GET /shorturls/stats/{shortcode}` — Get analytics for a shortcode
- `GET /{shortcode}` — Redirect to original URL (tracks click)

## Troubleshooting
- Ensure MongoDB is running locally on port 27017
- If ports are in use, update them in `application.properties` (backend) or Vite config (frontend)
- CORS is enabled for `http://localhost:5173` in backend config
 
