package com.playtomic.tests.wallet.infrastructure.jpa;

import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase.FindWalletQuery;
import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class WalletDbRepository implements WalletRepository {

  private WalletJpaRepository walletJpaRepository;
  private ModelMapper modelMapper;

  @Override
  public Optional<Wallet> findWalletById(FindWalletQuery findWalletQuery) {
    Optional<WalletEntity> walletEntityOpt = walletJpaRepository.findById(findWalletQuery.getId());
    return walletEntityOpt.map(walletEntity -> modelMapper.map(walletEntity, Wallet.class));
  }
}
