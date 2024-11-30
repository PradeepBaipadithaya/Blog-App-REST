# Steps to Run Spring Boot Blog App
## 1. Maven Build the Project
If you have installed Maven on your machine then use the below command:
```
mvn clean package
```
If you haven't insatlled Maven on your machine then use below command:
```
./mvnw clean package
 ```
Note: Go to root directory of the project and execute above command.
## 2. Create a Database
Before running Spring boot blog application, you need to create the MySQL database.

Use the below SQL database to create the MySQL database:
 ```sql
 create database myblog
 ```
Database name - myblog
## 3. Run Spring Boot Project
Use below command to run Spring boot application:
 ```
 mvn spring-boot:run
 ```
Once you run Spring boot application, Hibernate will create the database tables autimatically.
## 4. Insert Data
User below Insert SQL statements to insert records into roles table:
```sql
INSERT INTO `myblog.roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');
```
## 5. Create Admin User
User below Insert SQL statements to insert Admin User in DB:
```sql
INSERT INTO myblog.users (id, email, name, password, username) VALUES(1, 'admin@gmail.com', 'admin', '$2a$10$6ociEwymKsXrUqWzPNOiTOl8zuOQfIMl/aXqS5taI8XVArlfIVpQm', 'admin');
INSERT INTO myblog.users_roles (user_id, role_id) VALUES(1, 1);
```
## 6. Start Redis 
User below cmds:
```sql
> brew install redis
> brew services start redis
> redis-cli CONFIG GET port
```
Now, Spring boot blog application is ready to use.
