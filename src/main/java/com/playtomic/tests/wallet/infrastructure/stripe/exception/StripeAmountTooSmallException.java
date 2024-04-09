package com.playtomic.tests.wallet.infrastructure.stripe.exception;

public class StripeAmountTooSmallException extends StripeServiceException {

  public StripeAmountTooSmallException(String message) {
    super(message);
  }
}
