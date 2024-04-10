package com.playtomic.tests.wallet.infrastructure.jpa;

import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class WalletDbRepository implements WalletRepository {

  private WalletJpaRepository walletJpaRepository;
  private ModelMapper modelMapper;

  @Override
  public Optional<Wallet> findWalletById(UUID id) {
    Optional<WalletEntity> walletEntityOpt = walletJpaRepository.findById(id);
    return walletEntityOpt.map(walletEntity -> modelMapper.map(walletEntity, Wallet.class));
  }

  @Override
  public Wallet updateWallet(Wallet wallet) {
    WalletEntity walletEntity = modelMapper.map(wallet, WalletEntity.class);
    WalletEntity savedWalletEntity = walletJpaRepository.save(walletEntity);
    return modelMapper.map(savedWalletEntity, Wallet.class);
  }
}
