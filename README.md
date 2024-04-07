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

## How to run tests

````bash
./mvnw clean install
./mvnw test
````
