
# Job Listing REST API

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-brightgreen)
![Maven](https://img.shields.io/badge/build-maven-red)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

A complete and secure RESTful API for a job listing platform, built with Spring Boot, Spring Security, and MongoDB. This project demonstrates a real-world application architecture with user authentication, role-based access control, and full CRUD (Create, Read, Update, Delete) functionality for job postings.

## ✨ Core Features

-   **Secure User Authentication:** Stateless user registration and login using **JSON Web Tokens (JWT)**.
-   **Role-Based Access Control (RBAC):**
    -   👤 **`APPLICANT` Role:** Can view all job listings and apply for jobs.
    -   🏢 **`EMPLOYER` Role:** Can post new job listings and view the list of applicants for their jobs.
-   **Job Management:** Full CRUD operations for job listings (protected by role).
-   **Application Workflow:** A complete flow for an applicant to apply to a job and for an employer to view that application.
-   **Scalable Architecture:** Clean, decoupled architecture using Controller, Service, and Repository layers.

## 🛠️ Technology Stack

-   **Backend:**
    -   **Java 17:** The core programming language.
    -   **Spring Boot 3.x:** Framework for building the application.
    -   **Spring Web:** For creating RESTful API endpoints.
    -   **Spring Security:** For handling authentication and role-based authorization.
    -   **Spring Data MongoDB:** For seamless integration with the MongoDB database.
-   **Database:**
    -   **MongoDB:** A flexible NoSQL database for storing user and job data.
-   **Build & Tooling:**
    -   **Maven:** Project build and dependency management.
    -   **Docker:** For running the MongoDB instance in an isolated container.
    -   **JJWT (Java JWT):** A library for creating and validating JSON Web Tokens.
    -   **Lombok:** A library to reduce boilerplate code (getters, setters, etc.).

## 📋 Prerequisites

Before you begin, ensure you have the following installed on your system:
-   **Java Development Kit (JDK) 17** or newer
-   **Apache Maven** 3.6+
-   **Docker Desktop**
-   An API testing client like **Postman** (recommended) or `curl`.

## 🚀 Getting Started

Follow these steps to get the application running on your local machine.

### 1. Clone the Repository

git clone
cd joblistingapi--Springboot


# 2. Start the MongoDB Database
This project requires a running MongoDB instance. The easiest way to start one is with Docker.
Important: Make sure Docker Desktop is running before executing the command.

# This command downloads the Mongo image (if not present) and starts a container named "job-api-mongo"
docker run -d -p 27017:27017 --name job-api-mongo mongo
Use code with caution.
Bash
To verify that the container is running, use the command docker ps. You should see job-api-mongo in the list with a status of "Up".

3. Build and Run the Application
Use Maven to build the project and then run the executable JAR file.
Generated bash
# Build the project and create the JAR file
mvn clean install

# Run the application
java -jar target/joblistingapi-0.0.1-SNAPSHOT.jar

The application will start on http://localhost:8080 and will automatically connect to the MongoDB container.
API Endpoints
The base URL for all API endpoints is http://localhost:8080.

Authentication (/api/auth)
Method	Endpoint	Description	Access Control
POST	/api/auth/register	Register a new user.	Public
POST	/api/auth/login	Log in to get a JWT.	Public
Jobs (/api/jobs)

Method	Endpoint	Description	Access Control
GET	/api/jobs	Get a list of all jobs.	Public
GET	/api/jobs/{jobId}	Get details of a single job.	Public
POST	/api/jobs	Post a new job listing.	Employer Only
POST	/api/jobs/{jobId}/apply	Apply for a specific job.	Applicant Only
GET	/api/jobs/{jobId}/applicants	View all applicants for a job.	Employer Only



# 🏛️ Project Structure
The project follows a standard layered architecture to ensure separation of concerns.
src/main/java/com/joblisting/joblistingapi
model: Contains the data models (User, Job, Role). These are mapped to MongoDB documents.
repository: Contains the Spring Data MongoDB repository interfaces (JobRepository, UserRepository).
service: Contains the business logic (JobService, UserService).
controller: Contains the REST API controllers that handle incoming HTTP requests (JobController, AuthController).
security: Contains all Spring Security and JWT-related configurations (SecurityConfig, JwtUtil, etc.).
src/main/resources:
application.properties: Main configuration file for the application, including the database connection URI.


# 🔮 Future Improvements
Implement Update and Delete endpoints for jobs.
Add more robust validation for request bodies.
Implement a search and filtering API for jobs (e.g., by title, location).
Add pagination to the GET /api/jobs endpoint.
Write unit and integration tests for all layers.
Externalize sensitive configurations (like the JWT secret) using environment variables or a configuration server.

# 📄 License
This project is licensed under the MIT License. See the LICENSE file for details.

