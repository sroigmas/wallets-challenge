package com.playtomic.tests.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.playtomic.tests.wallet.service.Payment;
import com.playtomic.tests.wallet.service.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.StripeRestTemplateResponseErrorHandler;
import com.playtomic.tests.wallet.service.StripeService;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.RestTemplate;

class StripeServiceTest {

  private final URI chargesUri =
      URI.create("https://test.playtomic.io/v1/stripe-simulator/charges");

  private final URI refundsUri =
      URI.create(
          "https://test.playtomic.io/v1/stripe-simulator/payments/3ad80683-bc0a-483a-9e24-a7d0fff1c090/refunds");

  private final RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);

  @Test
  void givenPaymentData_whenCharging_thenCallsStripeService()
      throws NoSuchFieldException, IllegalAccessException {
    String creditCardNumber = "4242 4242 4242 4242";
    BigDecimal amount = new BigDecimal(15);

    RestTemplate restTemplate = mock(RestTemplate.class);
    when(restTemplateBuilder.errorHandler(any())).thenReturn(restTemplateBuilder);
    when(restTemplateBuilder.build()).thenReturn(restTemplate);
    StripeService stripeService = new StripeService(chargesUri, refundsUri, restTemplateBuilder);

    stripeService.charge(creditCardNumber, amount);

    ArgumentCaptor<Object> requestCaptor = ArgumentCaptor.forClass(Object.class);
    verify(restTemplate).postForObject(eq(chargesUri), requestCaptor.capture(), eq(Payment.class));

    Object request = requestCaptor.getValue();
    Field creditCardNumberfield = request.getClass().getDeclaredField("creditCardNumber");
    creditCardNumberfield.setAccessible(true);
    Field amountfield = request.getClass().getDeclaredField("amount");
    amountfield.setAccessible(true);

    assertEquals(creditCardNumber, creditCardNumberfield.get(request));
    assertEquals(amount, amountfield.get(request));
  }

  @Test
  void givenStatusUnprocessable_whenCharging_thenReturnsAmountTooSmallException()
      throws ClassNotFoundException {
    String creditCardNumber = "4242 4242 4242 4242";
    BigDecimal amount = new BigDecimal(5);

    RestTemplate restTemplate = mock(RestTemplate.class);
    when(restTemplateBuilder.errorHandler(any())).thenReturn(restTemplateBuilder);
    when(restTemplateBuilder.build()).thenReturn(restTemplate);
    StripeService stripeService = new StripeService(chargesUri, refundsUri, restTemplateBuilder);

    doAnswer(
            invocationOnMock -> {
              new StripeRestTemplateResponseErrorHandler()
                  .handleError(
                      new MockClientHttpResponse(
                          "Error".getBytes(), HttpStatus.UNPROCESSABLE_ENTITY));
              return null;
            })
        .when(restTemplate)
        .postForObject(
            eq(chargesUri),
            any(Class.forName("com.playtomic.tests.wallet.service.StripeService$ChargeRequest")),
            eq(Payment.class));

    assertThrows(
        StripeAmountTooSmallException.class, () -> stripeService.charge(creditCardNumber, amount));
  }
}
