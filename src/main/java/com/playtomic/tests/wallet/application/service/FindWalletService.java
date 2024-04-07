package com.playtomic.tests.wallet.application.service;

import com.playtomic.tests.wallet.application.exception.ApplicationNotFoundException;
import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase;
import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.domain.Wallet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindWalletService implements FindWalletUseCase {

  private WalletRepository walletRepository;

  @Override
  public Wallet findWalletById(FindWalletQuery findWalletQuery) {
    return walletRepository
        .findWalletById(findWalletQuery)
        .orElseThrow(() -> new ApplicationNotFoundException(String.format(
            "Wallet with id=%s could not be found.",
            findWalletQuery.getId())));
  }
}
