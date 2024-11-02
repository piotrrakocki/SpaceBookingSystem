# SpaceBookingSystem

## Table of Contents

- [Description](#description)
- [Technologies](#technologies)
- [Features](#features)
- [How to Run](#how-to-run)
- [Author](#author)

## Description

**SpaceBookingSystem** is a project for an application that allows users to book space flights. The application has been created to enable users to book space flights, manage reservations, browse available rockets, and add reviews after a successful flight. This system is user-friendly and offers features for both regular users and administrators.

## Technologies

The SpaceBookingSystem project is built using the following technologies:
- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Boot Mail
- Spring Security
- Hibernate Validator
- MySQL
- H2 Database
- Project Lombok
- DevTools
- HTML
- CSS
- Thymeleaf

## Features

##### Registration and Login
- Users can create a new account by providing their details.
- After registration, users must confirm their account via email within 15 minutes.
- Registered users can log in to access the application.

##### Space Flight Reservations
- Registered users can browse available space flights and make reservations.
- There is an option to cancel reservations if a user changes their mind.

##### Adding Flights by an Administrator
- Administrators have access to the functionality of adding new space flights to the system.
- Each flight can include details such as date, route, seat availability, cost, etc.

##### Rocket Management
- Administrators can add new rockets to the system.
- Users can browse available rockets and learn more about them before making a reservation.

##### Rocket Assignment to a Flight
- Each rocket is assigned to a specific space flight, allowing users to know which rocket they will use during their flight.

##### Flight Reviews
- Users who have completed a flight can add reviews.
- Reviews allow other users to learn about the experiences of others and help in making reservation decisions.

## How to Run
1. Make Sure You have Java Installed on your computer.
2. Clone this repository to your computer
3. Go to the project folder and run the application with the command:
```./mvnw spring-boot:run```
4. The Application will run on port 8080. 
5. You can now Open Your Browser and go to http://localhost:8080/home

## Author

The project was created by Piotr Rakocki.
