package com.playtomic.tests.wallet.infrastructure.stripe.exception;

public class StripeServiceException extends RuntimeException {

  public StripeServiceException(String message) {
    super(message);
  }
}
