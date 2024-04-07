package com.playtomic.tests.wallet.application.port.output;

import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase.FindWalletQuery;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Optional;

public interface WalletRepository {

  Optional<Wallet> findWalletById(FindWalletQuery findWalletQuery);
}
