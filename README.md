# Room Reservation System API

This is a RESTful API for a Room Reservation System built with Spring Boot and PostgreSQL, enabling users to create, read, update, and delete reservations efficiently. It combines Spring Boot’s structured architecture with PostgreSQL’s reliable data storage for seamless reservation management.

<p>
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
</p>

---

## 🚀 Tech Stack & Architecture

- **Spring Boot**: Handles HTTP requests and manages application components for a robust API framework.
- **Spring Security**: Secures the API endpoints with authentication and authorization mechanisms.
- **Spring Data JPA**: Simplifies database interactions and provides an abstraction layer over Hibernate.
- **Spring Session Redis**: Manages user sessions efficiently, allowing for scalable session handling.
- **PostgreSQL**: Stores reservation data reliably with strong data integrity.
- **Redis**: Caches frequently accessed data to improve performance and reduce load.
- **Docker**: Containerizes the application for consistent deployment across environments.
- **Vertical Slice Architecture**: Organizes features into modular slices with all necessary components per functionality.

---

## 📦 Features

- **User Booking Management**: Guests can create, view, update, and cancel their own reservations.
- **Admin Dashboard Operations**: Manage users, bookings, and rooms with elevated privileges.
- **Role-Based Access Control (RBAC)**: Supports `guest` and `admin` roles; admins can view all user's bookings.
- **Reporting**: Generate reports on revenue(total, monthly, etc), room availability, total bookings, and total rooms, etc.
- **Cursor-based Pagination**: Efficiently browse rooms with paginated results.
- **Check-In/Check-Out Dates**: Track and manage reservation dates accurately.
- **Cached Data**: Frequently accessed information is stored temporarily to provide faster responses.
- **Google Recaptcha**: Protects the API from bots and ensures that only human users can access certain endpoints. (with WebClient)

---

## 📸 Sample API Endpoints and Responses

### Room List and Info with Pagination
![Room List with Pagination](images/rooms-paginated.PNG)
![Room Info](images/room-info.PNG)

### Bookings
![Booking History](images/bookings-history.PNG)
![User Bookings](images/user-bookings.PNG)

### Reports
![Reports](images/report-summary.PNG)

---

## ⚠️ Notes

- This project is focused on backend development, and does not include a frontend interface. It is designed to be consumed by a frontend application or used for API testing and integration purposes.
- It is also for project learning and development, and may not be suitable for production use without considering additional factors. 
- Online payment gateways(e.g Stripe) are out of scope due to regional availability constraints, and the project is focused on Backend logic and API development.

---

## 🧠 Backend Practices

- **N+1 Query Prevention**: Eliminated via LAZY loading + EntityGraph for optimized fetch plans.
- **Efficient Data Access**: Proper indexing and query design to reduce DB load.
- **Caching Strategy**: Redis used to cache frequently accessed data.
- **Cursor-based Pagination**: Enables efficient and scalable room listing by loading records in chunks using cursors instead of fetching all at once.
- **Global Exception Handling**: Centralized error handling for consistent API responses.
- **RBAC Enforcement**: Secured endpoints based on user roles (guest/admin).

## 🛠️ Setup Instructions
- Clone repository 
- Configure environment variables in docker-compose.yml (or application.yml)
- Run `docker compose up` to start the application
