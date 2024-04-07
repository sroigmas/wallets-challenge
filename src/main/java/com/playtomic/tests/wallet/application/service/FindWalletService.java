package com.playtomic.tests.wallet.application.service;

import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase;
import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Optional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindWalletService implements FindWalletUseCase {

  private WalletRepository walletRepository;

  @Override
  public Optional<Wallet> findWalletById(FindWalletQuery findWalletQuery) {
    return walletRepository.findWalletById(findWalletQuery);
  }
}
