package com.playtomic.tests.wallet.application.service;

import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Optional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindWalletService implements FindWalletUseCase {

  @Override
  public Optional<Wallet> findWalletById(FindWalletQuery findWalletQuery) {
    return Optional.empty(); // TODO implement
  }
}
