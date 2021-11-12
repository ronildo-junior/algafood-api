# AlgaFood API
This project is based on AlgaWorks' Specialist Spring REST Training. The API is designed to be an MVP for Food Delivery, using DDD - Domain-Driven Design and best practice.

## Status
This project is currently under development.

## Technology
- [Java 11](https://adoptopenjdk.net/) - AdoptOpenJDK distribution.
- [Maven](https://maven.apache.org/) - Project Management.
- [MySQL 5.7](https://dev.mysql.com/doc/refman/5.7/en/) - Relational Database.
- [Spring Framework](https://spring.io/)
  - [Spring Boot 2.5.5](https://spring.io/projects/spring-boot) - Provides a start with an automatic configuration by convention.
  - [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - Provides repository support for the Java Persistence API (JPA).
  - [Spring Data Redis](https://spring.io/projects/spring-data-redis) - Provides easy configuration and access to Redis.
  - [Spring HATEOAS](https://spring.io/projects/spring-hateoas) - Provides some APIs to ease creating REST representations that follow the HATEOAS principle.
  - [Spring Security](https://spring.io/projects/spring-security) - Customizable authentication and access-control framework.
- [Redis](https://redis.io/) - In-memory data structure store, used as a key-value database, cache, and message broker.
- [Amazon AWS S3](https://aws.amazon.com/en/s3/) - Object storage service that stores data as objects within buckets.
- [Docker](https://www.docker.com/) - Open-source containerization platform. It enables developers to package applications into containers.
- [Flyway](https://flywaydb.org/) - Version control and migrations for database.
- [Jasper Reports](https://community.jaspersoft.com/) - Open-source Java reporting tool that can write to a variety of targets.
- [Loggly](https://www.loggly.com/) - Log cloud-based service.
- [Lombok](https://projectlombok.org/) - Generate boilerplate code.
- [MapStruct](https://mapstruct.org/) - Code generator for object mappings.
- [Nginx](https://www.nginx.com/) - Reverse proxy server.
- [SendGrid](https://sendgrid.com/) - Transactional email service.
- [SpringDoc](https://springdoc.org/) - Generation of API documentation.

## Running
Run the following command in a terminal window (in the complete) directory:
```text
./mvnw spring-boot:run
```
The project has at its root a file for import into Postman "postman_collection.json". Security and uri settings are centralized in the collection.

## Docker
The files to configure the docker are:
- Dockerfile;
- docker-compose.yml;
- .env (Environment Variables)

If you want to generate docker image, run the following command in a terminal window (in the complete) directory:
```text
./mvnw package -Pdocker
```
With docker image created, run the following command:
```text
docker-compose up
```
If you want to scale the application, run the following command:
```text
docker-compose up --scale algafood-api=NUMBER_OF_INSTANCES
```

## Integration Tests
The API has basic integration tests with a separated database. The tests use [JUnit 5](https://junit.org/junit5/docs/current/user-guide/) and [REST Assured](https://rest-assured.io/). The integration tests are disabled in build by [Maven Failsafe Plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/), to execute integration tests run the following command in a terminal window (in the complete) directory:
```text
./mvnw verify
```

## Endpoints
- Documentation: /index.html
- Root: /v1
- Cities: /v1/cities
- Cuisines: /v1/cuisines
- Orders: /v1/orders
- Payment-Methods: /v1/payment-methods
- Restaurants: /v1/restaurants
- Products: /v1/restaurants/{restaurantId}/products
- Product Photos: /v1/restaurants/{restaurantId}/products/{productId}/photo
- States: /v1/states
- Statistics: /v1/statistics
- User Groups: /v1/user-groups
- Users: /v1/users

## Infrastructure
- Amazon AWS S3 - Used to store product photos.
- Docker - Used to API containerization and load balancing (Poor man's Load Balancing).
- Jasper Reports - Used to daily sales report.
- Loggly - Used to store log information in cloud service.
- MySQL - Used to store all domain data.
- Nginx - Used as reverse proxy server.
- Redis - Used to store temporary authorization information. More specifically, in the Authorization Code Flow.
- SendGrid - Used to send transactional emails in order status transition.
  - Thymeleaf - used to renderer email HTML templates.

## Architecture
The application was designed using Domain-Driven Design concepts:
- Aggregate - Cluster of domain objects that can be treated as a single unit. Any references from outside the aggregate should only go to the aggregate root.
- Domain Events - Important Events that happens in the domain.
- Domain Service - Operations that are not directly related to an Entity.
- Infrastructure Service - Operations that talk to external resources and are not part of the primary problem domain.
- Models/Entities - Objects that have an identity.
- Repository - Interface to retrieve/manage stored objects.
- Specification - Design pattern whereby business rules can be recombined by chaining the business rules together using boolean logic.
- Ubiquitous Language - Common language shared by domain experts.

## HTTP Status Codes
The API is standardized with the following Response Status: 
- 200 - Successful request with Response Body.
- 201 - Successful creation of Resource.
- 204 - Successful request without Response Body.
- 400 - Bad Request. The request is invalid.
- 401 - Unauthorized. Authentication is required.
- 403 - Forbidden. Authenticated but do not have permission.
- 404 - Resource not found.
- 406 - Not Acceptable. The API isn't capable to generate the content type specified in the request (Accept header).
- 409 - Conflict. The resource is in use by some kind of association or constraint.
- 415 - Unsupported Media Type. The API is not support the request media type.
- 422 - Unprocessable Entity. The request is valid but not allowed by the business rules.
- 500 - Internal server error. There was an unexpected error processing the request.

## Exceptions
The exceptions are centralized in ApiExceptionHandler. All response error messages follow [RFC 7807](https://datatracker.ietf.org/doc/html/rfc7807), example:
```text
{
  "status": 409,
  "type": "http://api.algafood.local/conflict",
  "title": "Conflict",
  "detail": "Cuisine with name Maktubs already registered.",
  "userMessage": "Cuisine with name Maktubs already registered.",
  "timestamp": "2021-10-10T10:00:00.0000000Z"
}
 ```

## HATEOAS
The API uses a constraint HATEOAS of the REST architecture. It is used to facilitate API discoverability, where each response body has links to each resource/feature. The Links are displayed according to the user permissions. Example: 
```text
{
    "id": 1,
    "name": "Mineira",
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8080/v1/cuisines/1"
        },
        {
            "rel": "cuisine-list",
            "href": "http://localhost:8080/v1/cuisines"
        }
    ]
}
```
The default format HAL is disabled. To enable format HAL in response, add the following request header:
```text
--header 'Accept: application/hal+json' \
```
Example in HAL format:
```text
{
    "id": 1,
    "name": "Mineira",
    "_links": {
        "self": {
            "href": "http://api.algafood.local:8080/v1/cuisines/1"
        },
        "cuisine-list": {
            "href": "http://api.algafood.local:8080/v1/cuisines"
        }
    }
}
```

## Internationalization
The API uses Resource Bundle messages, which can be found in resources/i18n. The documentation has no translation, only the Response Messages. The supported languages are:
- English (Default Language)
- Brazilian Portuguese

To change response language, add the Request Parameter "lang", example:
```text
http://localhost:8080/v1/cuisines?lang=pt-BR
```

## Cache
The API uses Spring Web Shallow ETag Header filter, which automates ETag generation. If the "If-None-Match" header is present in the request, it intercepts the response and returns: 
- The body when the ETag is different;
- The HTTP Status 304 when the ETag has not changed.

The only exception is in Restaurant List Method, which verify the cache with last modification date. For testing only, wouldn't work in restaurant exclusion cases.

## Security
The API uses Spring Security Framework to automation. To access the endpoints is necessary a JWT signed with asymmetric key (RSA SHA-256). The JWT can be obtained by the following authorization flows:
- Authorization Code Flow with custom PKCE:
  - Used to security in external requests.
  - Requires client application credentials and registered user consent. Example:
    ```text
    http://localhost:8080/oauth/authorize?response_type=code&client_id=food-analytics&redirect_uri=http://127.0.0.1:8082&code_challenge=abcd&code_challenge_method=plain
    ```
    And use the obtained code in a new request to get the JWT token. Example:
    ```text
    curl --location --request POST 'localhost:8080/oauth/token' \
         --header 'Authorization: Basic Zm9vZC1hbmFseXRpY3M6YW5hbHl0aWNzQDEyMw==' \
         --header 'Content-Type: application/x-www-form-urlencoded' \
         --data-urlencode 'code=AUTHORIZATION_CODE_HERE' \
         --data-urlencode 'grant_type=authorization_code' \
         --data-urlencode 'redirect_uri=http://127.0.0.1:8082' \
         --data-urlencode 'code_verifier=abcd' 
    ```
- Password Flow:
  - Used to security in external requests.
  - Requires client application credentials and a registered user. Example:
    ```text
    curl --location --request POST 'localhost:8080/oauth/token' \
         --header 'Authorization: Basic YWxnYWZvb2Qtd2ViOndlYjEyMw==' \
         --header 'Content-Type: application/x-www-form-urlencoded' \
         --data-urlencode 'username=admin@algafood.com' \
         --data-urlencode 'password=123' \
         --data-urlencode 'grant_type=password'
    ```

## User Permission Control
The JWT contains all available user permissions of the authenticated user. The API check the access permissions on each endpoint.

## Profiles
The application has three accepted profiles:
- dev - Development (Default Profile)
  - Security is disabled.
  - Photo Storage is set to Local
  - Sending email is set to Fake (Only print email body on Console).
- homo - Homologation (Default in Docker Compose)
  - Security is enabled with OAuth2.
  - Photo Storage is set to AWS S3.
    - Keys are injected by IDE, or in .env file on Docker Compose.
  - Sending email is set to Sandbox (one specific recipient).
- prod - Production
  - Security is enabled with OAuth2.
  - Photo Storage is set to AWS S3.
    - Keys are injected by IDE, or in .env file on Docker Compose.
  - Sending email is set to SMTP with real user emails.

## Additional Configurations
Store type in session temporary information:
```text
spring.session.store-type=local or redis
```
Photo Storage Type
- Local
  ```text
  algafood.storage.storage-type=local
  algafood.storage.local.photos-path=THE_PATH_HERE
  ```
- AWS S3
  ```text
  algafood.storage.storage-type=s3
  algafood.storage.s3.access-key=ACCESS_KEY_HERE
  algafood.storage.s3.secret-key=SECRET_KEY_HERE
  algafood.storage.s3.bucket=BUCKET_NAME
  algafood.storage.s3.photos-directory=PHOTOS_DIRECTORY
  algafood.storage.s3.region=REGION_HERE
  ```
Email Configuration
- Fake
  ```text
  algafood.email.impl=fake
  algafood.email.sender=EMAIL_SENDER
  ```
- Sandbox
  ```text
  algafood.email.impl=smtp_sandbox
  algafood.email.sandbox.recipient=RECIPIENT_EMAIL_HERE
  algafood.email.sender=SENDER_EMAIL_HERE
  ## SMTP SendGrid
  spring.mail.host=smtp.sendgrid.net
  spring.mail.port=587
  spring.mail.username=apikey
  spring.mail.password=API_KEY_HERE
  ```
- SMTP (Production)
  ```text
  algafood.email.impl=smtp
  algafood.email.sender=SENDER_EMAIL_HERE
  ## SMTP SendGrid
  spring.mail.host=smtp.sendgrid.net
  spring.mail.port=587
  spring.mail.username=apikey
  spring.mail.password=API_KEY_HERE
  ```
Loggly Token
```text
logging.loggly.token=TOKEN_HERE
```