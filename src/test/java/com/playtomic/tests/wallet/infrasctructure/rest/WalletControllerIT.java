package com.playtomic.tests.wallet.infrasctructure.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class WalletControllerIT {

  @Autowired private MockMvc mvc;

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
}
