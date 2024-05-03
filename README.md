# User Service


RESTful API based on the web Spring Boot application: controller, responsible for the resource named Users

Functionality:
-
1. Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file;
2. Update one/some user fields;
3. Update all user fields;
4. Delete user;
5. Search for users by birth date range. Add the validation which checks that “From” is less than “To”.  Should return a list of objects;

Installation and launch instructions:
-
1. Clone the repository - git clone https://github.com/MaksymMedvedchuk/UserService.git
2. Make clean and build project - ./gradlew clean build
3. Run docker-compose file - docker-compose up


SWAGGER UI
-
http://localhost:8080/swagger-ui/index.html#/
-
Stack
-
Java 17, Spring(Boot, Core, Data), RESTfull API, Hibernate, PostgreSQL, Gradle, Docker, JUnit, Mockito

