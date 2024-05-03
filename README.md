User Service
-

RESTful API based on the web Spring Boot application: controller, responsible for the resource named Users

Functionality:

1. Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file;
2. Update one/some user fields;
3. Update all user fields;
4. Delete user;
5. Search for users by birth date range. Add the validation which checks that “From” is less than “To”.  Should return a list of objects;

SWAGGER UI

http://localhost:8080/swagger-ui/index.html#/
