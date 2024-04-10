package com.playtomic.tests.wallet.infrasctructure.rest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.infrastructure.jpa.WalletEntity;
import com.playtomic.tests.wallet.infrastructure.jpa.WalletJpaRepository;
import com.playtomic.tests.wallet.infrastructure.rest.PaymentRequest;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 9999)
class WalletControllerIT {

  @Autowired private MockMvc mvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired private WalletJpaRepository walletJpaRepository;

  @Test
  void givenWalletId_whenGettingWallet_thenReturnsWallet() throws Exception {
    String id = "3ad80683-bc0a-483a-9e24-a7d0fff1c090";

    ResultActions result = mvc.perform(get("/api/v1/wallets/{id}", id)).andDo(print());

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.balance").value("12.50"));
  }

  @Test
  void givenNonExistingWalletId_whenGettingWallet_thenReturnsNotFound() throws Exception {
    String id = "3ad80683-bc0a-483a-9e24-a7d0fff1c099";

    ResultActions result = mvc.perform(get("/api/v1/wallets/{id}", id)).andDo(print());

    result
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value("NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("Wallet with id=" + id + " could not be found."));
  }

  @Test
  void givenWrongUuid_whenGettingWallet_thenReturnsBadRequest() throws Exception {
    String id = "1";

    ResultActions result = mvc.perform(get("/api/v1/wallets/{id}", id)).andDo(print());

    result
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("id should be of type java.util.UUID"));
  }

  @Test
  void givenPaymentData_whenChargingWallet_thenChargesWallet() throws Exception {
    String id = "3ad80683-bc0a-483a-9e24-a7d0fff1c090";
    PaymentRequest paymentRequest =
        PaymentRequest.builder().amount("12.5").creditCardNumber("4242 4242 4242 4242").build();

    String stripeRequest =
        """
        {
            "credit_card": "4242 4242 4242 4242",
            "amount": 12.5
        }
        """;
    String stripeResponse =
        """
        {
            "id": "2ddae2b4-a8f6-41b7-9d8d-2b98acc62e66",
            "amount": 12.5
        }
        """;
    stubFor(
        post(urlEqualTo("/v1/stripe-simulator/charges"))
            .withRequestBody(containing(stripeRequest))
            .willReturn(aResponse().withBody(stripeResponse)));

    ResultActions result =
        mvc.perform(
                put("/api/v1/wallets/{id}/charge", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentRequest)))
            .andDo(print());

    result
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.balance").value("25.00"));

    Optional<WalletEntity> walletEntityOpt = walletJpaRepository.findById(UUID.fromString(id));
    assertTrue(walletEntityOpt.isPresent());
    assertEquals(25, walletEntityOpt.get().getBalance().doubleValue());
  }

  @Test
  void givenWrongNumber_whenChargingWallet_thenReturnsBadRequest() throws Exception {
    String id = "3ad80683-bc0a-483a-9e24-a7d0fff1c090";
    PaymentRequest paymentRequest =
        PaymentRequest.builder().amount("wrong").creditCardNumber("4242 4242 4242 4242").build();

    ResultActions result =
        mvc.perform(
                put("/api/v1/wallets/{id}/charge", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentRequest)))
            .andDo(print());

    result.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"));
  }
}
