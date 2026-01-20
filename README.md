# Hotel Review System — Backend (Microservices)

This repository contains a Java-based microservices backend for a Hotel Review System. The implementation is split into multiple Spring Boot services that communicate via service discovery (Eureka) and REST calls. The project demonstrates service separation for Users, Hotels and Ratings, an API Gateway and a Service Registry.

Important note: This README is based on the code present in the repository. There is no authentication/authorization implemented in the current codebase — all services expose public endpoints. Do not use this code in production without adding proper security.

Table of contents
- Project overview
- Services included
- Key features (implemented)
- Technology stack
- Project structure
- Running the system (local)
- Important endpoints (what exists in code)
- Persistence and configuration
- Tests
- Development notes / TODO
- Contributing
- License

Project overview
----------------
The repo implements a small microservice ecosystem for managing users, hotels and ratings. Services register with a Eureka Service Registry and can call each other (RestTemplate + LoadBalanced, and Feign is enabled in the user service). The User service aggregates Rating data (from Rating Service) and Hotel data (from Hotel Service) to return enriched user details.

Services included
-----------------
- ServiceRegistry — Eureka server for service discovery
  - Located in: ServiceRegistry/
  - Main class: ServiceRegistryApplication
- ApiGateway — simple gateway module (discovery client)
  - Located in: ApiGateway/
  - Main class: ApiGatewayApplication
- UserService — manages users and aggregates ratings & hotel info
  - Located in: UserService/
  - Main class: UserServiceApplication
  - Uses: Spring Data JPA repository (UserRepository), RestTemplate (LoadBalanced), Feign clients enabled
- HotelService — manages hotel data
  - Located in: HotelService/
  - Main class: HotelServiceApplication
  - Hotel entity implemented
- RatingService — manages ratings
  - Located in: RatingService/
  - Main class: RatingServiceApplication

Key features (implemented from code)
------------------------------------
- Service discovery via Eureka (ServiceRegistry + @EnableDiscoveryClient on services).
- User creation and retrieval.
  - UserService generates a UUID for new users and persists them via JPA.
  - Retrieving a user (GET user by id) calls RATING-SERVICE to fetch ratings for that user, then calls HOTEL-SERVICE to fetch hotel details per rating and returns aggregated data.
- Rating service basic CRUD:
  - Create rating
  - Get all ratings
  - Get ratings by userId
  - Get ratings by hotelId
- Hotel entity and persistence (HotelService contains Hotel entity).
- Spring Data JPA repositories for Users and Ratings.

Technology stack (observed in code)
-----------------------------------
- Java (project language)
- Spring Boot
- Spring Cloud Netflix Eureka (service registry / discovery)
- Spring Cloud OpenFeign (enabled in UserService)
- RestTemplate with @LoadBalanced (UserService/MyConfig)
- Spring Data JPA (repositories)
- Lombok (entities use Lombok annotations)
- Maven (typical Spring project layout; build with mvn)

Project structure (top-level modules)
------------------------------------
- ApiGateway/           — gateway module
- ServiceRegistry/      — Eureka server
- UserService/          — user management & aggregation
  - src/main/java/com/nav/user/service/...
  - Entities: User, Rating (transient model used for aggregation), Hotel (transient)
  - Repositories: UserRepository
  - Service implementations: UserServiceImpl
  - Config: MyConfig (provides LoadBalanced RestTemplate)
- HotelService/         — hotel CRUD & entity
  - Entities: Hotel
- RatingService/        — rating CRUD & repository

How to run locally
------------------
Prerequisites:
- JDK 11+ (17 recommended)
- Maven 3.6+
- A relational database if you change JPA config (the projects use JPA but do not force a specific DB in this README)
- (Optional) Docker if you want to containerize services

Run order (recommended)
1. Start ServiceRegistry (Eureka) first:
   - cd ServiceRegistry
   - mvn spring-boot:run
2. Start supporting services (hotel and rating) so they can register:
   - cd HotelService; mvn spring-boot:run
   - cd RatingService; mvn spring-boot:run
3. Start UserService (it calls RatingService & HotelService):
   - cd UserService; mvn spring-boot:run
4. Start ApiGateway (optional):
   - cd ApiGateway; mvn spring-boot:run

You can also build JARs and run them:
- mvn clean package
- java -jar target/<module>-0.0.1-SNAPSHOT.jar

Because services rely on discovery, ensure each service's application.properties/yml references the Eureka server (or run with localhost/eureka configured).

Important endpoints (implemented / referenced in code)
-----------------------------------------------------
The README below lists endpoints which are present or referenced in the code. Use these as starting points — consult each service's controller classes for exact method signatures.

RatingService (observed in code)
- POST /ratings                 — create a rating
- GET /ratings                  — get all ratings
- GET /ratings/users/{userId}   — get ratings for a user (used by UserService)
- GET /ratings/hotels/{hotelId} — get ratings for a hotel

UserService (observed in code)
- POST /users                   — create a user (UserService.saveUser)
- GET /users                    — get all users (UserService.getAllUser)
- GET /users/{userId}           — get a single user, aggregates ratings and hotel details:
  - UserService calls: http://RATING-SERVICE/ratings/users/{userId} to fetch ratings
  - For each rating, UserService calls HOTEL-SERVICE to fetch hotel details (URL pattern in code comments: /hotels/{hotelId})

HotelService (observed entities & used by UserService)
- GET /hotels/{hotelId}         — get hotel details (used by UserService)

Persistence & configuration
---------------------------
- The project uses Spring Data JPA repositories (UserRepository and RatingRepository) and JPA entities (User, Hotel, Rating entities exist in appropriate modules).
- Configure each service's datasource via application.properties / application.yml for production usage (Postgres, MySQL, H2 for local testing, etc).
- MyConfig in UserService registers a @LoadBalanced RestTemplate to allow calls using service names (e.g., http://RATING-SERVICE).

Testing
-------
- Each Spring Boot module contains a simple context load test (SpringBootTest) under src/test.
- Run tests with: mvn test (from each module).


Contributing
------------
- Open an issue describing the change or improvement.
- Fork the repository and create a feature branch.
- Run tests locally and add tests for new behavior.
- Open a pull request with a clear description of changes.

License
-------
No license file detected in repository (please add a LICENSE file if you want to open-source this project). Consider MIT or Apache-2.0.

Contact
-------
Repository: https://github.com/souravdas142/Hotel-Review-System-Backend

