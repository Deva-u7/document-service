# Document Service Application

This is a Spring Boot application to write E2E Test cases for uploading files to AWS S3, 
saving document metadata to an H2 in-memory database.
Also added one wire mock test case to understand how to set up wire mock server and play with it.

## Features
- Upload a file to AWS S3 and save document metadata to H2 in-memory database.
- E2E tests with LocalStack for S3 and H2 database.
- Mocking with WireMock also.

## Prerequisites
- Java 21
- Maven
- Docker (for LocalStack)

## Configuration
Configure the application using `application.yml` files for different profiles.

1) For Upload in S3 configure following attribute in application.yml.
- access-key , secret-key , bucket name and region in
2) Test Profile will automaticaly generate above attributes.


## Technologies Used
- Integration  E2E Test For AWS S3.
- LocalStack for S3 integration (Testcontainers).
- WireMock for mocking.
- Spring Boot
- AWS S3 SDK
- H2 Database
- JUnit
- Maven
