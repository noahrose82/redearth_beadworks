# Red Earth Bead Works — Full-Stack Capstone (MySQL + MongoDB)

This project implements the architecture described in the **Red Earth Bead Works Final Architecture Plan (Milestone 3)**:
- React SPA (React Router + Axios)
- Spring Boot 3.x backend secured with Spring Security + JWT
- Dual-database approach:
  - **MySQL** (orders, users, payments, addresses)
  - **MongoDB** (catalog/product documents + flexible attributes)

> Note: Stripe integration is implemented as a **safe stub** for local development (no keys required).
> You can wire real Stripe Payment Intents later by setting environment variables and swapping the stub service.

## Prerequisites
- Docker Desktop
- Java 17+
- Maven 3.9+
- Node 18+

## Start Databases (MySQL + MongoDB)
```bash
cd infra
docker compose up -d
```

## Run Backend
```bash
cd backend
mvn spring-boot:run
```
Backend: http://localhost:8080

## Run Frontend
```bash
cd frontend
npm install
npm run dev
```
Frontend: http://localhost:5173

## Default Accounts
- Admin: `admin@redearth.local` / `Admin123!`
- Customer: `customer@redearth.local` / `Customer123!`

## Key API Routes
- Auth:
  - POST `/api/auth/register`
  - POST `/api/auth/login`
  - GET `/api/auth/me`
- Catalog (Mongo):
  - GET `/api/catalog/products`
  - GET `/api/catalog/products/{id}`
  - POST `/api/catalog/products` (ADMIN)
  - PUT `/api/catalog/products/{id}` (ADMIN)
  - DELETE `/api/catalog/products/{id}` (ADMIN)
- Orders/Checkout (MySQL):
  - POST `/api/orders` (create order from cart)
  - GET `/api/orders/mine`
  - GET `/api/orders/{id}`
  - POST `/api/checkout/intent` (creates a **stub** payment intent)
  - POST `/api/checkout/confirm` (confirms payment + stores Payment record)

## Project Layout
- `infra/` docker compose for MySQL & MongoDB
- `backend/` Spring Boot app (JWT auth, catalog, orders, payments)
- `frontend/` React SPA (routes: Home, Shop, Product, Cart, Checkout, Account, Admin)

## Notes
- Passwords stored with BCrypt hashing.
- JWT is stored in browser localStorage for this demo.
- CORS is enabled for localhost dev.
