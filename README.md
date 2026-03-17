# Room Reservation System API

This is a RESTful API for a Room Reservation System built with Spring Boot and PostgreSQL, enabling users to create, read, update, and delete reservations efficiently. It combines Spring Boot’s structured architecture with PostgreSQL’s reliable data storage for seamless reservation management.

<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
<img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white" />
<img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" />
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" />

## 🚀 Tech Stack & Architecture

- **Spring Boot**: Handles HTTP requests and manages application components for a robust API framework.
- **Spring Security**: Secures the API endpoints with authentication and authorization mechanisms.
- **Spring Data JPA**: Simplifies database interactions and provides an abstraction layer over Hibernate.
- **Spring Session Redis**: Manages user sessions efficiently, allowing for scalable session handling.
- **PostgreSQL**: Stores reservation data reliably with strong data integrity.
- **Redis**: Caches frequently accessed data to improve performance and reduce load.
- **Docker**: Containerizes the application for consistent deployment across environments.
- **Vertical Slice Architecture**: Organizes features into modular slices with all necessary components per functionality.

## 📦 Features

- **User Booking Management**: Guests can create, view, update, and cancel their own reservations.
- **Admin Dashboard Operations**: Manage users, bookings, and rooms with elevated privileges.
- **Role-Based Access Control (RBAC)**: Supports `guest` and `admin` roles; admins can view all user's bookings.
- **Reporting**: Generate reports on revenue, room availability, total bookings, and total rooms, etc.
- **Room Pagination**: Efficiently browse rooms with paginated results.
- **Check-In/Check-Out Dates**: Track and manage reservation dates accurately.
- **Cached Room Data**: Frequently accessed room information is cached for faster responses.

## ⚠️ Notes
- This project is focused on backend development, and does not include a frontend interface. It is designed to be consumed by a frontend application or used for API testing and integration purposes.
- It is also for project learning and development, and may not be suitable for production use without considering additional factors. 
- Online payment gateways(e.g Stripe) are out of scope due to regional availability constraints, and the project is focused on Backend logic and API development.