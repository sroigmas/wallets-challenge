# Wallets challenge

### Notes

- The followed approach tries to charge first the money against the Stripe service and then update
  the wallet balance in the database. Another approach could be doing it the other way around and
  making use of the annotation @Transactional in order to rollback the database changes in case the
  Stripe service is down. However, there would be a small time window when the user would have the
  balance updated without being charged yet
- The method 'refund' in class 'StripService' was fixed to call the refunds url (before it was
  calling the charges url)
- The unit test class 'StripeServiceTest' was fixed. To avoid changing the provided implementation,
  the tests use Mockito and Java reflection to mock and test both the happy path and the error one
- Hexagonal architecture is being used. Spring has been considered part of the infrastructure so
  there are no Spring annotations inside the 'application' folder. Use cases are being initialized
  inside 'infrastructure/configuration' folder

### Improvements

- The Stripe service doesn't allow charging an amount less than 10, so we could add a request
  validation (@Min) to do the same. This way we avoid database and service calls
- Docker could be added to use a Postgres database instead of H2 as well as do some tests using
  Testcontainers
- We could add some retry logic when trying to update the wallet in the database in case there is
  some database failure (@Retryable). Or we could send a Kafka event to do the database update for
  the same purpose

## How to run

````bash
./mvnw clean install
./mvnw spring-boot:run
````

You can check the OpenAPI documentation:

- Using Swagger UI: http://localhost:8090/swagger-ui/index.html
- In JSON format: http://localhost:8090/v3/api-docs
- In YAML format: http://localhost:8090/v3/api-docs.yaml

You can test the GET endpoint like this:
http://localhost:8090/api/v1/wallets/3ad80683-bc0a-483a-9e24-a7d0fff1c090

## How to run tests

````bash
./mvnw clean install
./mvnw test
````
