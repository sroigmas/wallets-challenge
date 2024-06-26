package com.playtomic.tests.wallet.application.port.input;

import com.playtomic.tests.wallet.domain.Wallet;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

public interface FindWalletUseCase {

  Wallet findWalletById(FindWalletQuery findWalletQuery);

  @Value
  @Builder
  class FindWalletQuery {
    UUID id;
  }
}
