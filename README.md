# Running and building car CRUD backend

This project is the backend part of car CRUD app

## Running the project locally for development and testing

Clone the project and cd into the created directory. Then to run the project use ```mvn```:

```
mvn spring-boot:run
```

## Building the project for production and running the build

To build the project with ```mvn``` run

```
mvn clean
mvn package
```

cd into target directory and run the jar file with ```java -jar```, for example

```
java -jar cardatabase-0.0.1-SNAPSHOT.jar
```


# Replacing H2 in-memory database with MySQL

From application.properties file remove line

```
spring.datasource.url=jdbc:h2:mem:testdb
```

Lines for H2 console (only used for development) configuration should also be removed:

```
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Add the following lines to application.properties to configure mysql connection:

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://<host>:<port>/cardb
spring.datasource.username=<username>
spring.datasource.password=<password>
```
Also remove H2 dependency from pom.xml

```
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

and add mysql connector dependency

```
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

For detailed example see 
[mysql branch](https://github.com/ToniVrdoljak/car-CRUD-backend/tree/feature/mysql-db) of this project.
