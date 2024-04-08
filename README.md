# Wallets challenge

### Notes

- The method 'refund' in class 'StripService' was fixed to call the refunds url (before it was
  calling the charges url)
- The unit test class 'StripeServiceTest' was fixed. To avoid changing the provided implementation,
  the tests use Mockito and Java reflection to mock and test both the happy path and the error one

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
