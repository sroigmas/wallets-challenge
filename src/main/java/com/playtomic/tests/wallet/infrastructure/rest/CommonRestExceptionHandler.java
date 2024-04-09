package com.playtomic.tests.wallet.infrastructure.rest;

import com.playtomic.tests.wallet.application.exception.ApplicationNotFoundException;
import com.playtomic.tests.wallet.infrastructure.exception.InfrastructureExternalException;
import com.playtomic.tests.wallet.infrastructure.stripe.exception.StripeServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class CommonRestExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMethodArgumentTypeMismatchExceptions(
      MethodArgumentTypeMismatchException e) {
    String message = e.getName() + " should be of type " + e.getRequiredType().getName();
    log.error(message);
    return new ErrorResponse(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(ApplicationNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleApplicationNotFoundExceptions(ApplicationNotFoundException e) {
    log.error(e.getMessage());
    return new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
  }

  @ExceptionHandler(InfrastructureExternalException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleInfrastructureExternalExceptions(InfrastructureExternalException e) {
    log.error(e.getMessage());
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }

  @ExceptionHandler(StripeServiceException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleStripeServiceExceptions(StripeServiceException e) {
    log.error(e.getMessage());
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleUnexpectedExceptions(Exception e) {
    log.error(e.getMessage());
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }
}
