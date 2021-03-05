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
