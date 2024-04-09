package com.playtomic.tests.wallet.infrastructure.stripe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentResponse {

  @NotNull private final String id;

  @JsonCreator
  public PaymentResponse(@JsonProperty(value = "id", required = true) String id) {
    this.id = id;
  }
}
