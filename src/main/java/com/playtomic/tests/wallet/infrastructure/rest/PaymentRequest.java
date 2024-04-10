package com.playtomic.tests.wallet.infrastructure.rest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentRequest {

  @NotBlank
  @Positive
  private String amount;

  @NotBlank
  @Pattern(regexp = "^\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}$")
  private String creditCardNumber;
}
