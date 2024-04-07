package com.playtomic.tests.wallet.infrastructure.jpa;

import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase.FindWalletQuery;
import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class WalletDbRepository implements WalletRepository {

  @Override
  public Optional<Wallet> findWalletById(FindWalletQuery findWalletQuery) {
    return Optional.empty(); // TODO implement
  }
}
