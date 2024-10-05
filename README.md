# auth-s3-user-management
_A Spring Boot app for managing user authentication with role-based access using Spring Security, integrated with Amazon S3 for image uploads._

## Features
- **User authentication**: Secure login with role-based access control (e.g., ADMIN, USER).
- **Amazon S3 integration**: Upload and manage user profile images stored in AWS S3.
- **Spring Security**: Protect endpoints and manage roles.
- **PostgreSQL**: Database support for storing user credentials and roles.

## Technologies Used
- **Spring Boot**
- **Spring Security**
- **Amazon S3**
- **PostgreSQL**
- **BCryptPasswordEncoder** for password encryption

## Getting Started

### Prerequisites
- **Java 17** or higher
- **PostgreSQL** (ensure it's running locally or provide a remote DB)
- **AWS S3**: You’ll need an S3 bucket and access credentials.
- **Maven** (or any other build tool)
