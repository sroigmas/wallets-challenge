package com.playtomic.tests.wallet.infrastructure.stripe;

import com.playtomic.tests.wallet.infrastructure.stripe.exception.StripeAmountTooSmallException;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class StripeRestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

  @Override
  protected void handleError(ClientHttpResponse response, HttpStatusCode statusCode)
      throws IOException {
    if (statusCode == HttpStatus.UNPROCESSABLE_ENTITY) {
      throw new StripeAmountTooSmallException("Error while making a request to Stripe");
    }

    super.handleError(response, statusCode);
  }
}
