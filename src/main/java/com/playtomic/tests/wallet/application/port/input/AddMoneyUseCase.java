package com.playtomic.tests.wallet.application.port.input;

import com.playtomic.tests.wallet.domain.Wallet;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

public interface AddMoneyUseCase {

  Wallet addMoney(AddMoneyCommand addMoneyCommand);

  @Value
  @Builder
  class AddMoneyCommand {
    UUID id;
    BigDecimal amount;
  }
}
