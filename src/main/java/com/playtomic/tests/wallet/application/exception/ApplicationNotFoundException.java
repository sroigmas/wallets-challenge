package com.playtomic.tests.wallet.application.exception;

public class ApplicationNotFoundException extends RuntimeException {

  public ApplicationNotFoundException(String message) {
    super(message);
  }
}
