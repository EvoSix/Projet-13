# Projet-13 Définissez une solution fonctionnelle et concevez l’architecture d’une application

## Project Overview

This repository contains a full-stack application split into two parts:
1. **Frontend**: Built with Angular 19.
2. **Backend**: A Spring Boot application for handling API requests and managing data.

---

## Prerequisites

### General Requirements
- **Node.js**: Version 20 or higher. [Download Node.js](https://nodejs.org/)
- **Angular CLI**: Version 19.2.8. Install it globally using:
```bash
npm install -g @angular/cli@19.2.8
```
- Java Development Kit (JDK): Version 17. Ensure JAVA_HOME is properly set.
- Maven: Version 3.6 or later. Install Maven
- MySQL Database: Install MySQL server locally or use a cloud-hosted MySQL database.
- Environment Variables
- 
### Backend Configuration
To configure the backend, modify the application.properties, modify the env variable for it to work.
```
# Database Configuration
DB_USERNAME=your_username
DB_PASSWORD=your_password
DB_URL=URL to your Database

SERVER_PORT= Port to communicate. Set it to 3001 is recommended
# JWT Configuration
JWT_SECRET=your_secret_key
JWT_EXPIRATION= time expiration in ms
```

### Setting Up the Database
- Install MySQL
- Start the MySQL server
- Create the data base
- run the Poc.sql from back/src/ressources

### Execute the backend
- install backend
```bash
cd back
mvn clean install
```
- Run the Backend
```bash
mvn spring-boot:run
```
The backend server will start at http://localhost:3001.

### Execute the frontend

- install frontend
```bash
cd front
npm install
```

- Run the frontend
Start the frontend server:
```bash
ng serve
```

The frontend application will run at http://localhost:4200.


