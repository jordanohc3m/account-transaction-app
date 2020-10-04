# Account Transaction App

A simple account transaction system.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

| Technologies   |  Version   | Required   | 
| -------------  |  ----------| ---------- | 
| Java           |  11        | True       | 
| Apache Maven   |  3.6.3     | True       |
| Docker         |  19.03.12  | True       | 
| Docker Compose |  1.26.2    | True       | 
 

### Installing

A step by step series of examples that tell you how to get a development env running

### Generate application image
```
sh generate-image.sh # -DskipTests to ignore tests
# or
mvn clean package 
docker build -f docker/Dockerfile -t pismo/account-app .
```

### Application (On container)
```
docker-compose -f docker/docker-compose.yml up -d account-app
```

### Application (Dev mode)
```
mvn clean spring-boot:run
```

After that:

| Applications            |  Address                                                                           | 
| ------------------------|  --------------------------------------------------------------------------------- | 
| account-app             |  http://localhost:8080                                                             | 
| API Doc                 |  http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config |
 

## Running the tests

To run unit tests:

```
mvn clean -Dgroups="unit-tests" test
```

To run integration tests:

```
mvn clean -Dgroups="integration-tests" test
```

To run all tests:

```
mvn clean test
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Docker](https://www.docker.com/) - A standardized unit of software

## Author

* **Jordano M. Gonçalves** - [GitHub](https://github.com/jordanohc3m)

