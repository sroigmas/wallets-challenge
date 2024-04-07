package com.playtomic.tests.wallet.service;

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
      throw new StripeAmountTooSmallException();
    }

    super.handleError(response, statusCode);
  }
}
