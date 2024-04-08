package com.playtomic.tests.wallet.application.port.output;

import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {

  Optional<Wallet> findWalletById(UUID id);

  Wallet updateWallet(Wallet wallet);
}
